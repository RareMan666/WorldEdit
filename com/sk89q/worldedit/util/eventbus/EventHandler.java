package com.sk89q.worldedit.util.eventbus;

import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationTargetException;

public abstract class EventHandler implements Comparable<EventHandler> {
   private final EventHandler.Priority priority;

   protected EventHandler(EventHandler.Priority priority) {
      Preconditions.checkNotNull(priority);
      this.priority = priority;
   }

   public EventHandler.Priority getPriority() {
      return this.priority;
   }

   public final void handleEvent(Object event) throws InvocationTargetException {
      try {
         this.dispatch(event);
      } catch (Throwable var3) {
         throw new InvocationTargetException(var3);
      }
   }

   public abstract void dispatch(Object var1) throws Exception;

   public int compareTo(EventHandler o) {
      return this.getPriority().ordinal() - o.getPriority().ordinal();
   }

   @Override
   public abstract int hashCode();

   @Override
   public abstract boolean equals(Object var1);

   @Override
   public String toString() {
      return "EventHandler{priority=" + this.priority + '}';
   }

   public static enum Priority {
      VERY_EARLY,
      EARLY,
      NORMAL,
      LATE,
      VERY_LATE;
   }
}
