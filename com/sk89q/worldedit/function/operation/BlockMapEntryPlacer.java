package com.sk89q.worldedit.function.operation;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.Extent;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class BlockMapEntryPlacer implements Operation {
   private final Extent extent;
   private final Iterator<Entry<BlockVector, BaseBlock>> iterator;

   public BlockMapEntryPlacer(Extent extent, Iterator<Entry<BlockVector, BaseBlock>> iterator) {
      Preconditions.checkNotNull(extent);
      Preconditions.checkNotNull(iterator);
      this.extent = extent;
      this.iterator = iterator;
   }

   @Override
   public Operation resume(RunContext run) throws WorldEditException {
      while (this.iterator.hasNext()) {
         Entry<BlockVector, BaseBlock> entry = this.iterator.next();
         this.extent.setBlock(entry.getKey(), entry.getValue());
      }

      return null;
   }

   @Override
   public void cancel() {
   }

   @Override
   public void addStatusMessages(List<String> messages) {
   }
}
