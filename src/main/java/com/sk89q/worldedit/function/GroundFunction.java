package com.sk89q.worldedit.function;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.mask.Mask;

public class GroundFunction implements LayerFunction {
   private Mask mask;
   private final RegionFunction function;
   private int affected;

   public GroundFunction(Mask mask, RegionFunction function) {
      Preconditions.checkNotNull(mask);
      Preconditions.checkNotNull(function);
      this.mask = mask;
      this.function = function;
   }

   public Mask getMask() {
      return this.mask;
   }

   public void setMask(Mask mask) {
      Preconditions.checkNotNull(mask);
      this.mask = mask;
   }

   public int getAffected() {
      return this.affected;
   }

   @Override
   public boolean isGround(Vector position) {
      return this.mask.test(position);
   }

   @Override
   public boolean apply(Vector position, int depth) throws WorldEditException {
      if (depth == 0 && this.function.apply(position)) {
         this.affected++;
      }

      return false;
   }
}
