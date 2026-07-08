package com.sk89q.worldedit.function.mask;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.Extent;
import javax.annotation.Nullable;

public class ExistingBlockMask extends AbstractExtentMask {
   public ExistingBlockMask(Extent extent) {
      super(extent);
   }

   @Override
   public boolean test(Vector vector) {
      return this.getExtent().getLazyBlock(vector).getType() != 0;
   }

   @Nullable
   @Override
   public Mask2D toMask2D() {
      return null;
   }
}
