package com.sk89q.worldedit.extent;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.function.mask.Mask;

public class MaskingExtent extends AbstractDelegateExtent {
   private Mask mask;

   public MaskingExtent(Extent extent, Mask mask) {
      super(extent);
      Preconditions.checkNotNull(mask);
      this.mask = mask;
   }

   public Mask getMask() {
      return this.mask;
   }

   public void setMask(Mask mask) {
      Preconditions.checkNotNull(mask);
      this.mask = mask;
   }

   @Override
   public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
      return this.mask.test(location) && super.setBlock(location, block);
   }
}
