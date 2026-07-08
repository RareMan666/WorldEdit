package com.sk89q.worldedit.math.convolution;

public class GaussianKernel extends Kernel {
   public GaussianKernel(int radius, double sigma) {
      super(radius * 2 + 1, radius * 2 + 1, createKernel(radius, sigma));
   }

   private static float[] createKernel(int radius, double sigma) {
      int diameter = radius * 2 + 1;
      float[] data = new float[diameter * diameter];
      double sigma22 = 2.0 * sigma * sigma;
      double constant = Math.PI * sigma22;

      for (int y = -radius; y <= radius; y++) {
         for (int x = -radius; x <= radius; x++) {
            data[(y + radius) * diameter + x + radius] = (float)(Math.exp(-(x * x + y * y) / sigma22) / constant);
         }
      }

      return data;
   }
}
