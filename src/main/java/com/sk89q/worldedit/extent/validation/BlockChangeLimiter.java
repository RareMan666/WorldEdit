package com.sk89q.worldedit.extent.validation;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;

public class BlockChangeLimiter extends AbstractDelegateExtent {
   private int limit;
   private int count = 0;

   public BlockChangeLimiter(Extent extent, int limit) {
      super(extent);
      this.setLimit(limit);
   }

   public int getLimit() {
      return this.limit;
   }

   public void setLimit(int limit) {
      Preconditions.checkArgument(limit >= -1, "limit >= -1 required");
      this.limit = limit;
   }

   public int getCount() {
      return this.count;
   }

   @Override
   public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
      if (this.limit >= 0) {
         if (this.count >= this.limit) {
            throw new MaxChangedBlocksException(this.limit);
         }

         this.count++;
      }

      return super.setBlock(location, block);
   }
}
