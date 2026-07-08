package com.sk89q.worldedit.patterns;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Deprecated
public class RandomFillPattern implements Pattern {
   private static final Random random = new Random();
   private List<BlockChance> blocks;

   public RandomFillPattern(List<BlockChance> blocks) {
      double max = 0.0;

      for (BlockChance block : blocks) {
         max += block.getChance();
      }

      List<BlockChance> finalBlocks = new ArrayList<>();
      double i = 0.0;

      for (BlockChance block : blocks) {
         double v = block.getChance() / max;
         i += v;
         finalBlocks.add(new BlockChance(block.getBlock(), i));
      }

      this.blocks = finalBlocks;
   }

   @Override
   public BaseBlock next(Vector position) {
      double r = random.nextDouble();

      for (BlockChance block : this.blocks) {
         if (r <= block.getChance()) {
            return block.getBlock();
         }
      }

      throw new RuntimeException("ProportionalFillPattern");
   }

   @Override
   public BaseBlock next(int x, int y, int z) {
      return this.next(null);
   }
}
