package com.sk89q.worldedit.math.interpolation;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import java.util.List;

public class LinearInterpolation implements Interpolation {
   private List<Node> nodes;

   @Override
   public void setNodes(List<Node> nodes) {
      Preconditions.checkNotNull(nodes);
      this.nodes = nodes;
   }

   @Override
   public Vector getPosition(double position) {
      if (this.nodes == null) {
         throw new IllegalStateException("Must call setNodes first.");
      } else if (position > 1.0) {
         return null;
      } else {
         position *= this.nodes.size() - 1;
         int index1 = (int)Math.floor(position);
         double remainder = position - index1;
         Vector position1 = this.nodes.get(index1).getPosition();
         Vector position2 = this.nodes.get(index1 + 1).getPosition();
         return position1.multiply(1.0 - remainder).add(position2.multiply(remainder));
      }
   }

   @Override
   public Vector get1stDerivative(double position) {
      if (this.nodes == null) {
         throw new IllegalStateException("Must call setNodes first.");
      } else if (position > 1.0) {
         return null;
      } else {
         position *= this.nodes.size() - 1;
         int index1 = (int)Math.floor(position);
         Vector position1 = this.nodes.get(index1).getPosition();
         Vector position2 = this.nodes.get(index1 + 1).getPosition();
         return position2.subtract(position1);
      }
   }

   @Override
   public double arcLength(double positionA, double positionB) {
      if (this.nodes == null) {
         throw new IllegalStateException("Must call setNodes first.");
      } else if (positionA > positionB) {
         return this.arcLength(positionB, positionA);
      } else {
         positionA *= this.nodes.size() - 1;
         positionB *= this.nodes.size() - 1;
         int indexA = (int)Math.floor(positionA);
         double remainderA = positionA - indexA;
         int indexB = (int)Math.floor(positionB);
         double remainderB = positionB - indexB;
         return this.arcLengthRecursive(indexA, remainderA, indexB, remainderB);
      }
   }

   private double arcLengthRecursive(int indexA, double remainderA, int indexB, double remainderB) {
      switch (indexB - indexA) {
         case 0:
            return this.arcLengthRecursive(indexA, remainderA, remainderB);
         case 1:
            return this.arcLengthRecursive(indexA, remainderA, 1.0) + this.arcLengthRecursive(indexB, 0.0, remainderB);
         default:
            return this.arcLengthRecursive(indexA, remainderA, indexB - 1, 1.0) + this.arcLengthRecursive(indexB, 0.0, remainderB);
      }
   }

   private double arcLengthRecursive(int index, double remainderA, double remainderB) {
      Vector position1 = this.nodes.get(index).getPosition();
      Vector position2 = this.nodes.get(index + 1).getPosition();
      return position1.distance(position2) * (remainderB - remainderA);
   }

   @Override
   public int getSegment(double position) {
      if (this.nodes == null) {
         throw new IllegalStateException("Must call setNodes first.");
      } else if (position > 1.0) {
         return Integer.MAX_VALUE;
      } else {
         position *= this.nodes.size() - 1;
         return (int)Math.floor(position);
      }
   }
}
