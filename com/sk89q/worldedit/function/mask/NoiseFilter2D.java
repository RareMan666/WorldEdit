package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.math.noise.NoiseGenerator;

public class NoiseFilter2D extends AbstractMask2D {
   private NoiseGenerator noiseGenerator;
   private double density;

   public NoiseFilter2D(NoiseGenerator noiseGenerator, double density) {
      this.setNoiseGenerator(noiseGenerator);
      this.setDensity(density);
   }

   public NoiseGenerator getNoiseGenerator() {
      return this.noiseGenerator;
   }

   public void setNoiseGenerator(NoiseGenerator noiseGenerator) {
      Preconditions.checkNotNull(noiseGenerator);
      this.noiseGenerator = noiseGenerator;
   }

   public double getDensity() {
      return this.density;
   }

   public void setDensity(double density) {
      Preconditions.checkArgument(density >= 0.0, "density must be >= 0");
      Preconditions.checkArgument(density <= 1.0, "density must be >= 1");
      this.density = density;
   }

   @Override
   public boolean test(Vector2D pos) {
      return this.noiseGenerator.noise(pos) <= this.density;
   }
}
