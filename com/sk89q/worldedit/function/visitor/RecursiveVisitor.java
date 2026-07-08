package com.sk89q.worldedit.function.visitor;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.mask.Mask;

public class RecursiveVisitor extends BreadthFirstSearch {
   private final Mask mask;

   public RecursiveVisitor(Mask mask, RegionFunction function) {
      super(function);
      Preconditions.checkNotNull(mask);
      this.mask = mask;
   }

   @Override
   protected boolean isVisitable(Vector from, Vector to) {
      return this.mask.test(to);
   }
}
