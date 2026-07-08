package com.sk89q.worldedit.math.noise;

import net.royawesome.jlibnoise.module.source.RidgedMulti;

public class RidgedMultiFractalNoise extends JLibNoiseGenerator<RidgedMulti> {
   protected RidgedMulti createModule() {
      return new RidgedMulti();
   }

   public double getFrequency() {
      return this.getModule().getFrequency();
   }

   public void setFrequency(double frequency) {
      this.getModule().setFrequency(frequency);
   }

   public double getLacunarity() {
      return this.getModule().getLacunarity();
   }

   public void setLacunarity(double lacunarity) {
      this.getModule().setLacunarity(lacunarity);
   }

   public int getOctaveCount() {
      return this.getModule().getOctaveCount();
   }

   public void setOctaveCount(int octaveCount) {
      this.getModule().setOctaveCount(octaveCount);
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
