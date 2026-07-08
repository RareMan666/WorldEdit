package com.sk89q.worldedit.function.pattern;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPattern extends AbstractPattern {
   private final Random random = new Random();
   private List<RandomPattern.Chance> patterns = new ArrayList<>();
   private double max = 0.0;

   public void add(Pattern pattern, double chance) {
      Preconditions.checkNotNull(pattern);
      this.patterns.add(new RandomPattern.Chance(pattern, chance));
      this.max += chance;
   }

   @Override
   public BaseBlock apply(Vector position) {
      double r = this.random.nextDouble();
      double offset = 0.0;

      for (RandomPattern.Chance chance : this.patterns) {
         if (r <= (offset + chance.getChance()) / this.max) {
            return chance.getPattern().apply(position);
         }

         offset += chance.getChance();
      }

      throw new RuntimeException("ProportionalFillPattern");
   }

   private static class Chance {
      private Pattern pattern;
      private double chance;

      private Chance(Pattern pattern, double chance) {
         this.pattern = pattern;
         this.chance = chance;
      }

      public Pattern getPattern() {
         return this.pattern;
      }

      public double getChance() {
         return this.chance;
      }
   }
}
