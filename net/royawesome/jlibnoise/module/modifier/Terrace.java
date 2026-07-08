package net.royawesome.jlibnoise.module.modifier;

import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Terrace extends Module {
   int controlPointCount = 0;
   boolean invertTerraces = false;
   double[] ControlPoints = new double[0];

   public Terrace() {
      super(1);
   }

   public boolean isInvertTerraces() {
      return this.invertTerraces;
   }

   public void setInvertTerraces(boolean invertTerraces) {
      this.invertTerraces = invertTerraces;
   }

   public int getControlPointCount() {
      return this.controlPointCount;
   }

   public double[] getControlPoints() {
      return this.ControlPoints;
   }

   public void AddControlPoint(double value) {
      int insertionPos = this.FindInsertionPos(value);
      this.InsertAtPos(insertionPos, value);
   }

   public void ClearAllControlPoints() {
      this.ControlPoints = null;
      this.controlPointCount = 0;
   }

   public void MakeControlPoints(int controlPointCount) {
      if (controlPointCount < 2) {
         throw new IllegalArgumentException("Must have more than 2 control points");
      } else {
         this.ClearAllControlPoints();
         double terraceStep = 2.0 / (controlPointCount - 1.0);
         double curValue = -1.0;

         for (int i = 0; i < controlPointCount; i++) {
            this.AddControlPoint(curValue);
            curValue += terraceStep;
         }
      }
   }

   protected int FindInsertionPos(double value) {
      int insertionPos;
      for (insertionPos = 0; insertionPos < this.controlPointCount && !(value < this.ControlPoints[insertionPos]); insertionPos++) {
         if (value == this.ControlPoints[insertionPos]) {
            throw new IllegalArgumentException("Value must be unique");
         }
      }

      return insertionPos;
   }

   protected void InsertAtPos(int insertionPos, double value) {
      double[] newControlPoints = new double[this.controlPointCount + 1];

      for (int i = 0; i < this.controlPointCount; i++) {
         if (i < insertionPos) {
            newControlPoints[i] = this.ControlPoints[i];
         } else {
            newControlPoints[i + 1] = this.ControlPoints[i];
         }
      }

      this.ControlPoints = newControlPoints;
      this.controlPointCount++;
      this.ControlPoints[insertionPos] = value;
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
         double sourceModuleValue = this.SourceModule[0].GetValue(x, y, z);
         int indexPos = 0;

         while (indexPos < this.controlPointCount && !(sourceModuleValue < this.ControlPoints[indexPos])) {
            indexPos++;
         }

         int index0 = Utils.ClampValue(indexPos - 1, 0, this.controlPointCount - 1);
         int index1 = Utils.ClampValue(indexPos, 0, this.controlPointCount - 1);
         if (index0 == index1) {
            return this.ControlPoints[index1];
         } else {
            double value0 = this.ControlPoints[index0];
            double value1 = this.ControlPoints[index1];
            double alpha = (sourceModuleValue - value0) / (value1 - value0);
            if (this.invertTerraces) {
               alpha = 1.0 - alpha;
               double temp = value0;
               value0 = value1;
               value1 = temp;
            }

            alpha *= alpha;
            return Utils.LinearInterp(value0, value1, alpha);
         }
      }
   }
}
