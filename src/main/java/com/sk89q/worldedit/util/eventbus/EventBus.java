package com.sk89q.worldedit.util.eventbus;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventBus {
   private final Logger logger = Logger.getLogger(EventBus.class.getCanonicalName());
   private final SetMultimap<Class<?>, EventHandler> handlersByType = Multimaps.newSetMultimap(new HashMap(), new Supplier<Set<EventHandler>>() {
      public Set<EventHandler> get() {
         return EventBus.this.newHandlerSet();
      }
   });
   private final SubscriberFindingStrategy finder = new AnnotatedSubscriberFinder();
   private HierarchyCache flattenHierarchyCache = new HierarchyCache();

   public synchronized void subscribe(Class<?> clazz, EventHandler handler) {
      Preconditions.checkNotNull(clazz);
      Preconditions.checkNotNull(handler);
      this.handlersByType.put(clazz, handler);
   }

   public synchronized void subscribeAll(Multimap<Class<?>, EventHandler> handlers) {
      Preconditions.checkNotNull(handlers);
      this.handlersByType.putAll(handlers);
   }

   public synchronized void unsubscribe(Class<?> clazz, EventHandler handler) {
      Preconditions.checkNotNull(clazz);
      Preconditions.checkNotNull(handler);
      this.handlersByType.remove(clazz, handler);
   }

   public synchronized void unsubscribeAll(Multimap<Class<?>, EventHandler> handlers) {
      Preconditions.checkNotNull(handlers);

      for (Entry<Class<?>, Collection<EventHandler>> entry : handlers.asMap().entrySet()) {
         Set<EventHandler> currentHandlers = this.getHandlersForEventType(entry.getKey());
         Collection<EventHandler> eventMethodsInListener = entry.getValue();
         if (currentHandlers != null && !currentHandlers.containsAll(entry.getValue())) {
            currentHandlers.removeAll(eventMethodsInListener);
         }
      }
   }

   public void register(Object object) {
      this.subscribeAll(this.finder.findAllSubscribers(object));
   }

   public void unregister(Object object) {
      this.unsubscribeAll(this.finder.findAllSubscribers(object));
   }

   public void post(Object event) {
      List<EventHandler> dispatching = new ArrayList<>();
      synchronized (this) {
         for (Class<?> eventType : this.flattenHierarchy(event.getClass())) {
            Set<EventHandler> wrappers = this.getHandlersForEventType(eventType);
            if (wrappers != null && !wrappers.isEmpty()) {
               dispatching.addAll(wrappers);
            }
         }
      }

      Collections.sort(dispatching);

      for (EventHandler handler : dispatching) {
         this.dispatch(event, handler);
      }
   }

   protected void dispatch(Object event, EventHandler handler) {
      try {
         handler.handleEvent(event);
      } catch (InvocationTargetException var4) {
         this.logger.log(Level.SEVERE, "Could not dispatch event: " + event + " to handler " + handler, (Throwable)var4);
      }
   }

   synchronized Set<EventHandler> getHandlersForEventType(Class<?> type) {
      return this.handlersByType.get(type);
   }

   protected synchronized Set<EventHandler> newHandlerSet() {
      return new HashSet<>();
   }

   Set<Class<?>> flattenHierarchy(Class<?> concreteClass) {
      return this.flattenHierarchyCache.get(concreteClass);
   }
}
