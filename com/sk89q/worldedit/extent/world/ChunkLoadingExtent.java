package com.sk89q.worldedit.extent.world;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.world.World;

public class ChunkLoadingExtent extends AbstractDelegateExtent {
   private final World world;
   private boolean enabled;

   public ChunkLoadingExtent(Extent extent, World world, boolean enabled) {
      super(extent);
      Preconditions.checkNotNull(world);
      this.enabled = enabled;
      this.world = world;
   }

   public ChunkLoadingExtent(Extent extent, World world) {
      this(extent, world, true);
   }

   @Override
   public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
      this.world.checkLoadedChunk(location);
      return super.setBlock(location, block);
   }
}
