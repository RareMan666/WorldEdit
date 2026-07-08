package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import javax.annotation.Nullable;

public class BoundedHeightMask extends AbstractMask {
   private final int minY;
   private final int maxY;

   public BoundedHeightMask(int minY, int maxY) {
      Preconditions.checkArgument(minY <= maxY, "minY <= maxY required");
      this.minY = minY;
      this.maxY = maxY;
   }

   @Override
   public boolean test(Vector vector) {
      return vector.getY() >= this.minY && vector.getY() <= this.maxY;
   }

   @Nullable
   @Override
   public Mask2D toMask2D() {
      return null;
   }
}
