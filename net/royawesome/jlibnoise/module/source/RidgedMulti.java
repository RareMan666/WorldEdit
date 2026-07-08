package net.royawesome.jlibnoise.module.source;

import net.royawesome.jlibnoise.Noise;
import net.royawesome.jlibnoise.NoiseQuality;
import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.module.Module;

public class RidgedMulti extends Module {
   public static final double DEFAULT_RIDGED_FREQUENCY = 1.0;
   public static final double DEFAULT_RIDGED_LACUNARITY = 2.0;
   public static final int DEFAULT_RIDGED_OCTAVE_COUNT = 6;
   public static final NoiseQuality DEFAULT_RIDGED_QUALITY = NoiseQuality.STANDARD;
   public static final int DEFAULT_RIDGED_SEED = 0;
   public static final int RIDGED_MAX_OCTAVE = 30;
   double frequency = 1.0;
   double lacunarity = 2.0;
   NoiseQuality noiseQuality = DEFAULT_RIDGED_QUALITY;
   int octaveCount = 6;
   double[] SpectralWeights;
   int seed = 0;

   public RidgedMulti() {
      super(0);
      this.CalcSpectralWeights();
   }

   public double getFrequency() {
      return this.frequency;
   }

   public void setFrequency(double frequency) {
      this.frequency = frequency;
   }

   public double getLacunarity() {
      return this.lacunarity;
   }

   public void setLacunarity(double lacunarity) {
      this.lacunarity = lacunarity;
   }

   public NoiseQuality getNoiseQuality() {
      return this.noiseQuality;
   }

   public void setNoiseQuality(NoiseQuality noiseQuality) {
      this.noiseQuality = noiseQuality;
   }

   public int getOctaveCount() {
      return this.octaveCount;
   }

   public void setOctaveCount(int octaveCount) {
      this.octaveCount = Utils.GetMin(octaveCount, 30);
   }

   public int getSeed() {
      return this.seed;
   }

   public void setSeed(int seed) {
      this.seed = seed;
   }

   protected void CalcSpectralWeights() {
      double h = 1.0;
      double frequency = 1.0;
      this.SpectralWeights = new double[30];

      for (int i = 0; i < 30; i++) {
         this.SpectralWeights[i] = Math.pow(frequency, -h);
         frequency *= this.lacunarity;
      }
   }

   @Override
   public int GetSourceModuleCount() {
      return 0;
   }

   @Override
   public double GetValue(double x, double y, double z) {
      double x1 = x * this.frequency;
      double y1 = y * this.frequency;
      double z1 = z * this.frequency;
      double value = 0.0;
      double weight = 1.0;
      double offset = 1.0;
      double gain = 2.0;

      for (int curOctave = 0; curOctave < this.octaveCount; curOctave++) {
         double nx = Utils.MakeInt32Range(x1);
         double ny = Utils.MakeInt32Range(y1);
         double nz = Utils.MakeInt32Range(z1);
         int seed = this.seed + curOctave & 2147483647;
         double signal = Noise.GradientCoherentNoise3D(nx, ny, nz, seed, this.noiseQuality);
         signal = Math.abs(signal);
         signal = offset - signal;
         signal *= signal;
         signal *= weight;
         weight = signal * gain;
         if (weight > 1.0) {
            weight = 1.0;
         }

         if (weight < 0.0) {
            weight = 0.0;
         }

         value += signal * this.SpectralWeights[curOctave];
         x1 *= this.lacunarity;
         y1 *= this.lacunarity;
         z1 *= this.lacunarity;
      }

      return value * 1.25 - 1.0;
   }
}
