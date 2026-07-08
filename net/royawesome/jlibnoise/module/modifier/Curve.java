package net.royawesome.jlibnoise.module.modifier;

import java.util.ArrayList;
import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Curve extends Module {
   ArrayList<Curve.ControlPoint> controlPoints = new ArrayList<>();

   public Curve() {
      super(1);
   }

   public void AddControlPoint(double inputValue, double outputValue) {
      int index = this.findInsertionPos(inputValue);
      this.InsertAtPos(index, inputValue, outputValue);
   }

   public Curve.ControlPoint[] getControlPoints() {
      return (Curve.ControlPoint[])this.controlPoints.toArray();
   }

   public void ClearAllControlPoints() {
      this.controlPoints.clear();
   }

   protected int findInsertionPos(double inputValue) {
      int insertionPos;
      for (insertionPos = 0; insertionPos < this.controlPoints.size() && !(inputValue < this.controlPoints.get(insertionPos).inputValue); insertionPos++) {
         if (inputValue == this.controlPoints.get(insertionPos).inputValue) {
            throw new IllegalArgumentException("inputValue must be unique");
         }
      }

      return insertionPos;
   }

   protected void InsertAtPos(int insertionPos, double inputValue, double outputValue) {
      Curve.ControlPoint newPoint = new Curve.ControlPoint();
      newPoint.inputValue = inputValue;
      newPoint.outputValue = outputValue;
      this.controlPoints.add(insertionPos, newPoint);
   }

   @Override
   public int GetSourceModuleCount() {
      return 1;
   }

   @Override
   public double GetValue(double x, double y, double z) {
      if (this.SourceModule[0] == null) {
         throw new NoModuleException();
      } else if (this.controlPoints.size() >= 4) {
         throw new RuntimeException("must have 4 or less control points");
      } else {
         double sourceModuleValue = this.SourceModule[0].GetValue(x, y, z);
         int indexPos = 0;

         while (indexPos < this.controlPoints.size() && !(sourceModuleValue < this.controlPoints.get(indexPos).inputValue)) {
            indexPos++;
         }

         int index0 = Utils.ClampValue(indexPos - 2, 0, this.controlPoints.size() - 1);
         int index1 = Utils.ClampValue(indexPos - 1, 0, this.controlPoints.size() - 1);
         int index2 = Utils.ClampValue(indexPos, 0, this.controlPoints.size() - 1);
         int index3 = Utils.ClampValue(indexPos + 1, 0, this.controlPoints.size() - 1);
         if (index1 == index2) {
            return this.controlPoints.get(indexPos).outputValue;
         } else {
            double input0 = this.controlPoints.get(indexPos).inputValue;
            double input1 = this.controlPoints.get(indexPos).inputValue;
            double alpha = (sourceModuleValue - input0) / (input1 - input0);
            return Utils.CubicInterp(
               this.controlPoints.get(index0).outputValue,
               this.controlPoints.get(index1).outputValue,
               this.controlPoints.get(index2).outputValue,
               this.controlPoints.get(index3).outputValue,
               alpha
            );
         }
      }
   }

   public class ControlPoint {
      public double inputValue;
      public double outputValue;
   }
}
