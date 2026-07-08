package com.sk89q.worldedit.extent.world;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.world.World;

public class SurvivalModeExtent extends AbstractDelegateExtent {
   private final World world;
   private boolean toolUse = false;

   public SurvivalModeExtent(Extent extent, World world) {
      super(extent);
      Preconditions.checkNotNull(world);
      this.world = world;
   }

   public boolean hasToolUse() {
      return this.toolUse;
   }

   public void setToolUse(boolean toolUse) {
      this.toolUse = toolUse;
   }

   @Override
   public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
      if (this.toolUse && block.getType() == 0) {
         this.world.simulateBlockMine(location);
         return true;
      } else {
         return super.setBlock(location, block);
      }
   }
}
