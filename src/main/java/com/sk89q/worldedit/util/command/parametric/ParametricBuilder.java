package com.sk89q.worldedit.util.command.parametric;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.worldedit.util.auth.Authorizer;
import com.sk89q.worldedit.util.auth.NullAuthorizer;
import com.sk89q.worldedit.util.command.CommandCallable;
import com.sk89q.worldedit.util.command.CommandCompleter;
import com.sk89q.worldedit.util.command.Dispatcher;
import com.sk89q.worldedit.util.command.NullCompleter;
import com.sk89q.worldedit.util.command.binding.PrimitiveBindings;
import com.sk89q.worldedit.util.command.binding.StandardBindings;
import com.thoughtworks.paranamer.CachingParanamer;
import com.thoughtworks.paranamer.Paranamer;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParametricBuilder {
   private final Map<Type, Binding> bindings = new HashMap<>();
   private final Paranamer paranamer = new CachingParanamer();
   private final List<InvokeListener> invokeListeners = new ArrayList<>();
   private Authorizer authorizer = new NullAuthorizer();
   private CommandCompleter defaultCompleter = new NullCompleter();

   public ParametricBuilder() {
      this.addBinding(new PrimitiveBindings());
      this.addBinding(new StandardBindings());
   }

   public void addBinding(Binding binding, Type... type) {
      if (type == null || type.length == 0) {
         type = binding.getTypes();
      }

      for (Type t : type) {
         this.bindings.put(t, binding);
      }
   }

   public void addInvokeListener(InvokeListener listener) {
      this.invokeListeners.add(listener);
   }

   public void registerMethodsAsCommands(Dispatcher dispatcher, Object object) throws ParametricException {
      for (Method method : object.getClass().getDeclaredMethods()) {
         Command definition = method.getAnnotation(Command.class);
         if (definition != null) {
            CommandCallable callable = this.build(object, method, definition);
            dispatcher.registerCommand(callable, definition.aliases());
         }
      }
   }

   private CommandCallable build(Object object, Method method, Command definition) throws ParametricException {
      return new ParametricCallable(this, object, method, definition);
   }

   Paranamer getParanamer() {
      return this.paranamer;
   }

   Map<Type, Binding> getBindings() {
      return this.bindings;
   }

   List<InvokeListener> getInvokeListeners() {
      return this.invokeListeners;
   }

   public Authorizer getAuthorizer() {
      return this.authorizer;
   }

   public void setAuthorizer(Authorizer authorizer) {
      Preconditions.checkNotNull(authorizer);
      this.authorizer = authorizer;
   }

   public CommandCompleter getDefaultCompleter() {
      return this.defaultCompleter;
   }

   public void setDefaultCompleter(CommandCompleter defaultCompleter) {
      Preconditions.checkNotNull(defaultCompleter);
      this.defaultCompleter = defaultCompleter;
   }
}
