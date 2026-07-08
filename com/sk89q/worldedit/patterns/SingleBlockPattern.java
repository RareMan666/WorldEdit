package com.sk89q.worldedit.patterns;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;

@Deprecated
public class SingleBlockPattern implements Pattern {
   private BaseBlock block;

   public SingleBlockPattern(BaseBlock block) {
      this.block = block;
   }

   public BaseBlock getBlock() {
      return this.block;
   }

   @Override
   public BaseBlock next(Vector position) {
      return this.block;
   }

   @Override
   public BaseBlock next(int x, int y, int z) {
      return this.block;
   }
}
