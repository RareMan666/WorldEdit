package com.sk89q.worldedit.extent.world;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.RunContext;
import com.sk89q.worldedit.world.World;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FastModeExtent extends AbstractDelegateExtent {
   private final World world;
   private final Set<BlockVector2D> dirtyChunks = new HashSet<>();
   private boolean enabled = true;

   public FastModeExtent(World world) {
      this(world, true);
   }

   public FastModeExtent(World world, boolean enabled) {
      super(world);
      Preconditions.checkNotNull(world);
      this.world = world;
      this.enabled = enabled;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   @Override
   public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
      if (this.enabled) {
         this.dirtyChunks.add(new BlockVector2D(location.getBlockX() >> 4, location.getBlockZ() >> 4));
         return this.world.setBlock(location, block, false);
      } else {
         return this.world.setBlock(location, block, true);
      }
   }

   @Override
   protected Operation commitBefore() {
      return new Operation() {
         @Override
         public Operation resume(RunContext run) throws WorldEditException {
            if (!FastModeExtent.this.dirtyChunks.isEmpty()) {
               FastModeExtent.this.world.fixAfterFastMode(FastModeExtent.this.dirtyChunks);
            }

            return null;
         }

         @Override
         public void cancel() {
         }

         @Override
         public void addStatusMessages(List<String> messages) {
         }
      };
   }
}
