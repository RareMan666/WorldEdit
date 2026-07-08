package com.sk89q.worldedit.extent.logging;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;

public abstract class AbstractLoggingExtent extends AbstractDelegateExtent {
   protected AbstractLoggingExtent(Extent extent) {
      super(extent);
   }

   protected void onBlockChange(Vector position, BaseBlock newBlock) {
   }

   @Override
   public final boolean setBlock(Vector position, BaseBlock block) throws WorldEditException {
      this.onBlockChange(position, block);
      return super.setBlock(position, block);
   }
}
