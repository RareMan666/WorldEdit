package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.Extent;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;

public class BlockMask extends AbstractExtentMask {
   private final Set<BaseBlock> blocks = new HashSet<>();

   public BlockMask(Extent extent, Collection<BaseBlock> blocks) {
      super(extent);
      Preconditions.checkNotNull(blocks);
      this.blocks.addAll(blocks);
   }

   public BlockMask(Extent extent, BaseBlock... block) {
      this(extent, Arrays.asList(Preconditions.checkNotNull(block)));
   }

   public void add(Collection<BaseBlock> blocks) {
      Preconditions.checkNotNull(blocks);
      this.blocks.addAll(blocks);
   }

   public void add(BaseBlock... block) {
      this.add(Arrays.asList(Preconditions.checkNotNull(block)));
   }

   public Collection<BaseBlock> getBlocks() {
      return this.blocks;
   }

   @Override
   public boolean test(Vector vector) {
      BaseBlock block = this.getExtent().getBlock(vector);
      return this.blocks.contains(block) || this.blocks.contains(new BaseBlock(block.getType(), -1));
   }

   @Nullable
   @Override
   public Mask2D toMask2D() {
      return null;
   }
}
