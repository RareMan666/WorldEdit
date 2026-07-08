package com.sk89q.worldedit.math.transform;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.math.MathUtils;

public class AffineTransform implements Transform {
   private double m00;
   private double m01;
   private double m02;
   private double m03;
   private double m10;
   private double m11;
   private double m12;
   private double m13;
   private double m20;
   private double m21;
   private double m22;
   private double m23;

   public AffineTransform() {
      this.m00 = this.m11 = this.m22 = 1.0;
      this.m01 = this.m02 = this.m03 = 0.0;
      this.m10 = this.m12 = this.m13 = 0.0;
      this.m20 = this.m21 = this.m23 = 0.0;
   }

   public AffineTransform(double[] coefs) {
      if (coefs.length == 9) {
         this.m00 = coefs[0];
         this.m01 = coefs[1];
         this.m02 = coefs[2];
         this.m10 = coefs[3];
         this.m11 = coefs[4];
         this.m12 = coefs[5];
         this.m20 = coefs[6];
         this.m21 = coefs[7];
         this.m22 = coefs[8];
      } else {
         if (coefs.length != 12) {
            throw new IllegalArgumentException("Input array must have 9 or 12 elements");
         }

         this.m00 = coefs[0];
         this.m01 = coefs[1];
         this.m02 = coefs[2];
         this.m03 = coefs[3];
         this.m10 = coefs[4];
         this.m11 = coefs[5];
         this.m12 = coefs[6];
         this.m13 = coefs[7];
         this.m20 = coefs[8];
         this.m21 = coefs[9];
         this.m22 = coefs[10];
         this.m23 = coefs[11];
      }
   }

   public AffineTransform(double xx, double yx, double zx, double tx, double xy, double yy, double zy, double ty, double xz, double yz, double zz, double tz) {
      this.m00 = xx;
      this.m01 = yx;
      this.m02 = zx;
      this.m03 = tx;
      this.m10 = xy;
      this.m11 = yy;
      this.m12 = zy;
      this.m13 = ty;
      this.m20 = xz;
      this.m21 = yz;
      this.m22 = zz;
      this.m23 = tz;
   }

   @Override
   public boolean isIdentity() {
      if (this.m00 != 1.0) {
         return false;
      } else if (this.m11 != 1.0) {
         return false;
      } else if (this.m22 != 1.0) {
         return false;
      } else if (this.m01 != 0.0) {
         return false;
      } else if (this.m02 != 0.0) {
         return false;
      } else if (this.m03 != 0.0) {
         return false;
      } else if (this.m10 != 0.0) {
         return false;
      } else if (this.m12 != 0.0) {
         return false;
      } else if (this.m13 != 0.0) {
         return false;
      } else if (this.m20 != 0.0) {
         return false;
      } else {
         return this.m21 != 0.0 ? false : this.m23 == 0.0;
      }
   }

   public double[] coefficients() {
      return new double[]{this.m00, this.m01, this.m02, this.m03, this.m10, this.m11, this.m12, this.m13, this.m20, this.m21, this.m22, this.m23};
   }

   private double determinant() {
      return this.m00 * (this.m11 * this.m22 - this.m12 * this.m21)
         - this.m01 * (this.m10 * this.m22 - this.m20 * this.m12)
         + this.m02 * (this.m10 * this.m21 - this.m20 * this.m11);
   }

   public AffineTransform inverse() {
      double det = this.determinant();
      return new AffineTransform(
         (this.m11 * this.m22 - this.m21 * this.m12) / det,
         (this.m21 * this.m01 - this.m01 * this.m22) / det,
         (this.m01 * this.m12 - this.m11 * this.m02) / det,
         (
               this.m01 * (this.m22 * this.m13 - this.m12 * this.m23)
                  + this.m02 * (this.m11 * this.m23 - this.m21 * this.m13)
                  - this.m03 * (this.m11 * this.m22 - this.m21 * this.m12)
            )
            / det,
         (this.m20 * this.m12 - this.m10 * this.m22) / det,
         (this.m00 * this.m22 - this.m20 * this.m02) / det,
         (this.m10 * this.m02 - this.m00 * this.m12) / det,
         (
               this.m00 * (this.m12 * this.m23 - this.m22 * this.m13)
                  - this.m02 * (this.m10 * this.m23 - this.m20 * this.m13)
                  + this.m03 * (this.m10 * this.m22 - this.m20 * this.m12)
            )
            / det,
         (this.m10 * this.m21 - this.m20 * this.m11) / det,
         (this.m20 * this.m01 - this.m00 * this.m21) / det,
         (this.m00 * this.m11 - this.m10 * this.m01) / det,
         (
               this.m00 * (this.m21 * this.m13 - this.m11 * this.m23)
                  + this.m01 * (this.m10 * this.m23 - this.m20 * this.m13)
                  - this.m03 * (this.m10 * this.m21 - this.m20 * this.m11)
            )
            / det
      );
   }

   public AffineTransform concatenate(AffineTransform that) {
      double n00 = this.m00 * that.m00 + this.m01 * that.m10 + this.m02 * that.m20;
      double n01 = this.m00 * that.m01 + this.m01 * that.m11 + this.m02 * that.m21;
      double n02 = this.m00 * that.m02 + this.m01 * that.m12 + this.m02 * that.m22;
      double n03 = this.m00 * that.m03 + this.m01 * that.m13 + this.m02 * that.m23 + this.m03;
      double n10 = this.m10 * that.m00 + this.m11 * that.m10 + this.m12 * that.m20;
      double n11 = this.m10 * that.m01 + this.m11 * that.m11 + this.m12 * that.m21;
      double n12 = this.m10 * that.m02 + this.m11 * that.m12 + this.m12 * that.m22;
      double n13 = this.m10 * that.m03 + this.m11 * that.m13 + this.m12 * that.m23 + this.m13;
      double n20 = this.m20 * that.m00 + this.m21 * that.m10 + this.m22 * that.m20;
      double n21 = this.m20 * that.m01 + this.m21 * that.m11 + this.m22 * that.m21;
      double n22 = this.m20 * that.m02 + this.m21 * that.m12 + this.m22 * that.m22;
      double n23 = this.m20 * that.m03 + this.m21 * that.m13 + this.m22 * that.m23 + this.m23;
      return new AffineTransform(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23);
   }

   public AffineTransform preConcatenate(AffineTransform that) {
      double n00 = that.m00 * this.m00 + that.m01 * this.m10 + that.m02 * this.m20;
      double n01 = that.m00 * this.m01 + that.m01 * this.m11 + that.m02 * this.m21;
      double n02 = that.m00 * this.m02 + that.m01 * this.m12 + that.m02 * this.m22;
      double n03 = that.m00 * this.m03 + that.m01 * this.m13 + that.m02 * this.m23 + that.m03;
      double n10 = that.m10 * this.m00 + that.m11 * this.m10 + that.m12 * this.m20;
      double n11 = that.m10 * this.m01 + that.m11 * this.m11 + that.m12 * this.m21;
      double n12 = that.m10 * this.m02 + that.m11 * this.m12 + that.m12 * this.m22;
      double n13 = that.m10 * this.m03 + that.m11 * this.m13 + that.m12 * this.m23 + that.m13;
      double n20 = that.m20 * this.m00 + that.m21 * this.m10 + that.m22 * this.m20;
      double n21 = that.m20 * this.m01 + that.m21 * this.m11 + that.m22 * this.m21;
      double n22 = that.m20 * this.m02 + that.m21 * this.m12 + that.m22 * this.m22;
      double n23 = that.m20 * this.m03 + that.m21 * this.m13 + that.m22 * this.m23 + that.m23;
      return new AffineTransform(n00, n01, n02, n03, n10, n11, n12, n13, n20, n21, n22, n23);
   }

   public AffineTransform translate(Vector vec) {
      return this.translate(vec.getX(), vec.getY(), vec.getZ());
   }

   public AffineTransform translate(double x, double y, double z) {
      return this.concatenate(new AffineTransform(1.0, 0.0, 0.0, x, 0.0, 1.0, 0.0, y, 0.0, 0.0, 1.0, z));
   }

   public AffineTransform rotateX(double theta) {
      double cot = MathUtils.dCos(theta);
      double sit = MathUtils.dSin(theta);
      return this.concatenate(new AffineTransform(1.0, 0.0, 0.0, 0.0, 0.0, cot, -sit, 0.0, 0.0, sit, cot, 0.0));
   }

   public AffineTransform rotateY(double theta) {
      double cot = MathUtils.dCos(theta);
      double sit = MathUtils.dSin(theta);
      return this.concatenate(new AffineTransform(cot, 0.0, sit, 0.0, 0.0, 1.0, 0.0, 0.0, -sit, 0.0, cot, 0.0));
   }

   public AffineTransform rotateZ(double theta) {
      double cot = MathUtils.dCos(theta);
      double sit = MathUtils.dSin(theta);
      return this.concatenate(new AffineTransform(cot, -sit, 0.0, 0.0, sit, cot, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0));
   }

   public AffineTransform scale(double s) {
      return this.scale(s, s, s);
   }

   public AffineTransform scale(double sx, double sy, double sz) {
      return this.concatenate(new AffineTransform(sx, 0.0, 0.0, 0.0, 0.0, sy, 0.0, 0.0, 0.0, 0.0, sz, 0.0));
   }

   public AffineTransform scale(Vector vec) {
      return this.scale(vec.getX(), vec.getY(), vec.getZ());
   }

   @Override
   public Vector apply(Vector vector) {
      return new Vector(
         vector.getX() * this.m00 + vector.getY() * this.m01 + vector.getZ() * this.m02 + this.m03,
         vector.getX() * this.m10 + vector.getY() * this.m11 + vector.getZ() * this.m12 + this.m13,
         vector.getX() * this.m20 + vector.getY() * this.m21 + vector.getZ() * this.m22 + this.m23
      );
   }

   public AffineTransform combine(AffineTransform other) {
      return this.concatenate(other);
   }

   @Override
   public Transform combine(Transform other) {
      return (Transform)(other instanceof AffineTransform ? this.concatenate((AffineTransform)other) : new CombinedTransform(this, other));
   }

   @Override
   public String toString() {
      return String.format(
         "Affine[%g %g %g %g, %g %g %g %g, %g %g %g %g]}",
         this.m00,
         this.m01,
         this.m02,
         this.m03,
         this.m10,
         this.m11,
         this.m12,
         this.m13,
         this.m20,
         this.m21,
         this.m22,
         this.m23
      );
   }
}
