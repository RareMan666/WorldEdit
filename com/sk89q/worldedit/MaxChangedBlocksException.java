package com.sk89q.worldedit;

public class MaxChangedBlocksException extends WorldEditException {
   int maxBlocks;

   public MaxChangedBlocksException(int maxBlocks) {
      this.maxBlocks = maxBlocks;
   }

   public int getBlockLimit() {
      return this.maxBlocks;
   }
}
