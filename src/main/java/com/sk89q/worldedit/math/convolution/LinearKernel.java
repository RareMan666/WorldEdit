package com.sk89q.worldedit.math.convolution;

public class LinearKernel extends Kernel {
   public LinearKernel(int radius) {
      super(radius * 2 + 1, radius * 2 + 1, createKernel(radius));
   }

   private static float[] createKernel(int radius) {
      int diameter = radius * 2 + 1;
      float[] data = new float[diameter * diameter];
      int i = 0;

      while (i < data.length) {
         data[i++] = 1.0F / data.length;
      }

      return data;
   }
}
