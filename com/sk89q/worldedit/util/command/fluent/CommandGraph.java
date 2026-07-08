package com.sk89q.worldedit.util.command.fluent;

import com.sk89q.worldedit.util.command.Dispatcher;
import com.sk89q.worldedit.util.command.SimpleDispatcher;
import com.sk89q.worldedit.util.command.parametric.ParametricBuilder;

public class CommandGraph {
   private final DispatcherNode rootDispatcher;
   private ParametricBuilder builder;

   public CommandGraph() {
      SimpleDispatcher dispatcher = new SimpleDispatcher();
      this.rootDispatcher = new DispatcherNode(this, null, dispatcher);
   }

   public DispatcherNode commands() {
      return this.rootDispatcher;
   }

   public ParametricBuilder getBuilder() {
      return this.builder;
   }

   public CommandGraph builder(ParametricBuilder builder) {
      this.builder = builder;
      return this;
   }

   public Dispatcher getDispatcher() {
      return this.rootDispatcher.getDispatcher();
   }
}
