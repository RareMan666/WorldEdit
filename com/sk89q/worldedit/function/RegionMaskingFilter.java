package com.sk89q.worldedit.function;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.mask.Mask;

public class RegionMaskingFilter implements RegionFunction {
   private final RegionFunction function;
   private Mask mask;

   public RegionMaskingFilter(Mask mask, RegionFunction function) {
      Preconditions.checkNotNull(function);
      Preconditions.checkNotNull(mask);
      this.mask = mask;
      this.function = function;
   }

   @Override
   public boolean apply(Vector position) throws WorldEditException {
      return this.mask.test(position) && this.function.apply(position);
   }
}
