package com.sk89q.worldedit.math.noise;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import java.util.Random;

public class RandomNoise implements NoiseGenerator {
   private final Random random;

   public RandomNoise(Random random) {
      this.random = random;
   }

   public RandomNoise() {
      this(new Random());
   }

   @Override
   public float noise(Vector2D position) {
      return this.random.nextFloat();
   }

   @Override
   public float noise(Vector position) {
      return this.random.nextFloat();
   }
}
