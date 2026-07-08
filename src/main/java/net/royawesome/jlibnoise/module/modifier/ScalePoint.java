package net.royawesome.jlibnoise.module.modifier;

import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class ScalePoint extends Module {
   public static final double DEFAULT_SCALE_POINT_X = 1.0;
   public static final double DEFAULT_SCALE_POINT_Y = 1.0;
   public static final double DEFAULT_SCALE_POINT_Z = 1.0;
   double xScale = 1.0;
   double yScale = 1.0;
   double zScale = 1.0;

   public ScalePoint() {
      super(1);
   }

   public double getxScale() {
      return this.xScale;
   }

   public void setxScale(double xScale) {
      this.xScale = xScale;
   }

   public double getyScale() {
      return this.yScale;
   }

   public void setyScale(double yScale) {
      this.yScale = yScale;
   }

   public double getzScale() {
      return this.zScale;
   }

   public void setzScale(double zScale) {
      this.zScale = zScale;
   }

   @Override
   public int GetSourceModuleCount() {
      return 1;
   }

   @Override
   public double GetValue(double x, double y, double z) {
      if (this.SourceModule[0] == null) {
         throw new NoModuleException();
      } else {
         return this.SourceModule[0].GetValue(x * this.xScale, y * this.yScale, z * this.zScale);
      }
   }
}
