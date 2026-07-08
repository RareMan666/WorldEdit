package com.sk89q.worldedit.function.pattern;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.Extent;

public class RepeatingExtentPattern extends AbstractPattern {
   private Extent extent;
   private Vector offset;

   public RepeatingExtentPattern(Extent extent, Vector offset) {
      this.setExtent(extent);
      this.setOffset(offset);
   }

   public Extent getExtent() {
      return this.extent;
   }

   public void setExtent(Extent extent) {
      Preconditions.checkNotNull(extent);
      this.extent = extent;
   }

   public Vector getOffset() {
      return this.offset;
   }

   public void setOffset(Vector offset) {
      Preconditions.checkNotNull(offset);
      this.offset = offset;
   }

   @Override
   public BaseBlock apply(Vector position) {
      Vector base = position.add(this.offset);
      Vector size = this.extent.getMaximumPoint().subtract(this.extent.getMinimumPoint()).add(1, 1, 1);
      int x = base.getBlockX() % size.getBlockX();
      int y = base.getBlockY() % size.getBlockY();
      int z = base.getBlockZ() % size.getBlockZ();
      return this.extent.getBlock(new Vector(x, y, z));
   }
}
