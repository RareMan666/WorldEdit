package com.sk89q.worldedit.math.convolution;

public class Kernel {
   private int width;
   private int height;
   private int xOrigin;
   private int yOrigin;
   private float[] data;

   public Kernel(int width, int height, float[] data) {
      this.width = width;
      this.height = height;
      this.xOrigin = width - 1 >> 1;
      this.yOrigin = height - 1 >> 1;
      int len = width * height;
      if (data.length < len) {
         throw new IllegalArgumentException("Data array too small (is " + data.length + " and should be " + len);
      } else {
         this.data = new float[len];
         System.arraycopy(data, 0, this.data, 0, len);
      }
   }

   public final int getXOrigin() {
      return this.xOrigin;
   }

   public final int getYOrigin() {
      return this.yOrigin;
   }

   public final int getWidth() {
      return this.width;
   }

   public final int getHeight() {
      return this.height;
   }

   public final float[] getKernelData(float[] data) {
      if (data == null) {
         data = new float[this.data.length];
      } else if (data.length < this.data.length) {
         throw new IllegalArgumentException("Data array too small (should be " + this.data.length + " but is " + data.length + " )");
      }

      System.arraycopy(this.data, 0, data, 0, this.data.length);
      return data;
   }
}
