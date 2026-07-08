package com.sk89q.worldedit.math.noise;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import java.util.Random;
import net.royawesome.jlibnoise.module.Module;

abstract class JLibNoiseGenerator<V extends Module> implements NoiseGenerator {
   private static final Random RANDOM = new Random();
   private final V module = this.createModule();

   JLibNoiseGenerator() {
      this.setSeed(RANDOM.nextInt());
   }

   protected abstract V createModule();

   protected V getModule() {
      return this.module;
   }

   public abstract void setSeed(int var1);

   public abstract int getSeed();

   @Override
   public float noise(Vector2D position) {
      return this.forceRange(this.module.GetValue(position.getX(), 0.0, position.getZ()));
   }

   @Override
   public float noise(Vector position) {
      return this.forceRange(this.module.GetValue(position.getX(), position.getY(), position.getZ()));
   }

   private float forceRange(double value) {
      return (float)Math.max(0.0, Math.min(1.0, value / 2.0 + 0.5));
   }
}
