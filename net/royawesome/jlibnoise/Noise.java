package net.royawesome.jlibnoise;

public class Noise {
   public static final int X_NOISE_GEN = 1619;
   public static final int Y_NOISE_GEN = 31337;
   public static final int Z_NOISE_GEN = 6971;
   public static final int SEED_NOISE_GEN = 1013;
   public static final int SHIFT_NOISE_GEN = 8;

   public static double GradientCoherentNoise3D(double x, double y, double z, int seed, NoiseQuality quality) {
      int x0 = x > 0.0 ? (int)x : (int)x - 1;
      int x1 = x0 + 1;
      int y0 = y > 0.0 ? (int)y : (int)y - 1;
      int y1 = y0 + 1;
      int z0 = z > 0.0 ? (int)z : (int)z - 1;
      int z1 = z0 + 1;
      double xs;
      double ys;
      double zs;
      if (quality == NoiseQuality.FAST) {
         xs = x - x0;
         ys = y - y0;
         zs = z - z0;
      } else if (quality == NoiseQuality.STANDARD) {
         xs = Utils.SCurve3(x - x0);
         ys = Utils.SCurve3(y - y0);
         zs = Utils.SCurve3(z - z0);
      } else {
         xs = Utils.SCurve5(x - x0);
         ys = Utils.SCurve5(y - y0);
         zs = Utils.SCurve5(z - z0);
      }

      double n0 = GradientNoise3D(x, y, z, x0, y0, z0, seed);
      double n1 = GradientNoise3D(x, y, z, x1, y0, z0, seed);
      double ix0 = Utils.LinearInterp(n0, n1, xs);
      n0 = GradientNoise3D(x, y, z, x0, y1, z0, seed);
      n1 = GradientNoise3D(x, y, z, x1, y1, z0, seed);
      double ix1 = Utils.LinearInterp(n0, n1, xs);
      double iy0 = Utils.LinearInterp(ix0, ix1, ys);
      n0 = GradientNoise3D(x, y, z, x0, y0, z1, seed);
      n1 = GradientNoise3D(x, y, z, x1, y0, z1, seed);
      ix0 = Utils.LinearInterp(n0, n1, xs);
      n0 = GradientNoise3D(x, y, z, x0, y1, z1, seed);
      n1 = GradientNoise3D(x, y, z, x1, y1, z1, seed);
      ix1 = Utils.LinearInterp(n0, n1, xs);
      double iy1 = Utils.LinearInterp(ix0, ix1, ys);
      return Utils.LinearInterp(iy0, iy1, zs);
   }

   public static double GradientNoise3D(double fx, double fy, double fz, int ix, int iy, int iz, int seed) {
      int vectorIndex = 1619 * ix + 31337 * iy + 6971 * iz + 1013 * seed;
      vectorIndex ^= vectorIndex >> 8;
      vectorIndex &= 255;
      double xvGradient = Utils.RandomVectors[vectorIndex << 2];
      double yvGradient = Utils.RandomVectors[(vectorIndex << 2) + 1];
      double zvGradient = Utils.RandomVectors[(vectorIndex << 2) + 2];
      double xvPoint = fx - ix;
      double yvPoint = fy - iy;
      double zvPoint = fz - iz;
      return (xvGradient * xvPoint + yvGradient * yvPoint + zvGradient * zvPoint) * 2.12;
   }

   public static int IntValueNoise3D(int x, int y, int z, int seed) {
      int n = 1619 * x + 31337 * y + 6971 * z + 1013 * seed & 2147483647;
      n = n >> 13 ^ n;
      return n * (n * n * 60493 + 19990303) + 1376312589 & 2147483647;
   }

   public static double ValueCoherentNoise3D(double x, double y, double z, int seed, NoiseQuality quality) {
      int x0 = x > 0.0 ? (int)x : (int)x - 1;
      int x1 = x0 + 1;
      int y0 = y > 0.0 ? (int)y : (int)y - 1;
      int y1 = y0 + 1;
      int z0 = z > 0.0 ? (int)z : (int)z - 1;
      int z1 = z0 + 1;
      double xs;
      double ys;
      double zs;
      if (quality == NoiseQuality.FAST) {
         xs = x - x0;
         ys = y - y0;
         zs = z - z0;
      } else if (quality == NoiseQuality.STANDARD) {
         xs = Utils.SCurve3(x - x0);
         ys = Utils.SCurve3(y - y0);
         zs = Utils.SCurve3(z - z0);
      } else {
         xs = Utils.SCurve5(x - x0);
         ys = Utils.SCurve5(y - y0);
         zs = Utils.SCurve5(z - z0);
      }

      double n0 = ValueNoise3D(x0, y0, z0, seed);
      double n1 = ValueNoise3D(x1, y0, z0, seed);
      double ix0 = Utils.LinearInterp(n0, n1, xs);
      n0 = ValueNoise3D(x0, y1, z0, seed);
      n1 = ValueNoise3D(x1, y1, z0, seed);
      double ix1 = Utils.LinearInterp(n0, n1, xs);
      double iy0 = Utils.LinearInterp(ix0, ix1, ys);
      n0 = ValueNoise3D(x0, y0, z1, seed);
      n1 = ValueNoise3D(x1, y0, z1, seed);
      ix0 = Utils.LinearInterp(n0, n1, xs);
      n0 = ValueNoise3D(x0, y1, z1, seed);
      n1 = ValueNoise3D(x1, y1, z1, seed);
      ix1 = Utils.LinearInterp(n0, n1, xs);
      double iy1 = Utils.LinearInterp(ix0, ix1, ys);
      return Utils.LinearInterp(iy0, iy1, zs);
   }

   public static double ValueNoise3D(int x, int y, int z, int seed) {
      return 1.0 - IntValueNoise3D(x, y, z, seed) / 1.0737418E9F;
   }
}
