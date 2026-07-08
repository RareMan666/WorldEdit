package com.sk89q.worldedit.blocks;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.Extent;

public class LazyBlock extends BaseBlock {
   private final Extent extent;
   private final Vector position;
   private boolean loaded = false;

   public LazyBlock(int type, Extent extent, Vector position) {
      super(type);
      Preconditions.checkNotNull(extent);
      Preconditions.checkNotNull(position);
      this.extent = extent;
      this.position = position;
   }

   public LazyBlock(int type, int data, Extent extent, Vector position) {
      super(type, data);
      Preconditions.checkNotNull(extent);
      Preconditions.checkNotNull(position);
      this.extent = extent;
      this.position = position;
   }

   @Override
   public void setId(int id) {
      throw new UnsupportedOperationException("This object is immutable");
   }

   @Override
   public void setData(int data) {
      throw new UnsupportedOperationException("This object is immutable");
   }

   @Override
   public CompoundTag getNbtData() {
      if (!this.loaded) {
         BaseBlock loadedBlock = this.extent.getBlock(this.position);
         super.setNbtData(loadedBlock.getNbtData());
      }

      return super.getNbtData();
   }

   @Override
   public void setNbtData(CompoundTag nbtData) {
      throw new UnsupportedOperationException("This object is immutable");
   }
}
