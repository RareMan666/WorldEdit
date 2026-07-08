package com.sk89q.worldedit.math.noise;

import net.royawesome.jlibnoise.module.source.Voronoi;

public class VoronoiNoise extends JLibNoiseGenerator<Voronoi> {
   protected Voronoi createModule() {
      return new Voronoi();
   }

   public double getFrequency() {
      return this.getModule().getFrequency();
   }

   public void setFrequency(double frequency) {
      this.getModule().setFrequency(frequency);
   }

   @Override
   public void setSeed(int seed) {
      this.getModule().setSeed(seed);
   }

   @Override
   public int getSeed() {
      return this.getModule().getSeed();
   }
}
