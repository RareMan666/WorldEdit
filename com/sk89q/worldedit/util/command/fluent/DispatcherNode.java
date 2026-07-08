package com.sk89q.worldedit.util.command.fluent;

import com.sk89q.worldedit.util.command.CommandCallable;
import com.sk89q.worldedit.util.command.Dispatcher;
import com.sk89q.worldedit.util.command.SimpleDispatcher;
import com.sk89q.worldedit.util.command.parametric.ParametricBuilder;

public class DispatcherNode {
   private final CommandGraph graph;
   private final DispatcherNode parent;
   private final SimpleDispatcher dispatcher;

   DispatcherNode(CommandGraph graph, DispatcherNode parent, SimpleDispatcher dispatcher) {
      this.graph = graph;
      this.parent = parent;
      this.dispatcher = dispatcher;
   }

   public DispatcherNode describeAs(String description) {
      this.dispatcher.getDescription().setDescription(description);
      return this;
   }

   public DispatcherNode register(CommandCallable callable, String... alias) {
      this.dispatcher.registerCommand(callable, alias);
      return this;
   }

   public DispatcherNode registerMethods(Object object) {
      ParametricBuilder builder = this.graph.getBuilder();
      if (builder == null) {
         throw new RuntimeException("No ParametricBuilder set");
      } else {
         builder.registerMethodsAsCommands(this.getDispatcher(), object);
         return this;
      }
   }

   public DispatcherNode group(String... alias) {
      SimpleDispatcher command = new SimpleDispatcher();
      this.getDispatcher().registerCommand(command, alias);
      return new DispatcherNode(this.graph, this, command);
   }

   public DispatcherNode parent() {
      if (this.parent != null) {
         return this.parent;
      } else {
         throw new RuntimeException("This node does not have a parent");
      }
   }

   public CommandGraph graph() {
      return this.graph;
   }

   public Dispatcher getDispatcher() {
      return this.dispatcher;
   }
}
