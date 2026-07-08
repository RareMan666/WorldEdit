package com.sk89q.worldedit.extent.world;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.world.World;

public class BlockQuirkExtent extends AbstractDelegateExtent {
   private final World world;

   public BlockQuirkExtent(Extent extent, World world) {
      super(extent);
      Preconditions.checkNotNull(world);
      this.world = world;
   }

   @Override
   public boolean setBlock(Vector position, BaseBlock block) throws WorldEditException {
      BaseBlock lazyBlock = this.getExtent().getLazyBlock(position);
      int existing = lazyBlock.getType();
      if (BlockType.isContainerBlock(existing)) {
         this.world.clearContainerBlockContents(position);
      } else if (existing == 79) {
         this.world.setBlock(position, new BaseBlock(0));
      }

      return super.setBlock(position, block);
   }
}
