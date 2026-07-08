package com.sk89q.worldedit.math.interpolation;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.logging.Logger;

public class ReparametrisingInterpolation implements Interpolation {
   private static final Logger log = Logger.getLogger(ReparametrisingInterpolation.class.getCanonicalName());
   private final Interpolation baseInterpolation;
   private double totalArcLength;
   private final TreeMap<Double, Double> cache = new TreeMap<>();

   public ReparametrisingInterpolation(Interpolation baseInterpolation) {
      Preconditions.checkNotNull(baseInterpolation);
      this.baseInterpolation = baseInterpolation;
   }

   @Override
   public void setNodes(List<Node> nodes) {
      Preconditions.checkNotNull(nodes);
      this.baseInterpolation.setNodes(nodes);
      this.cache.clear();
      this.cache.put(0.0, 0.0);
      this.cache.put(this.totalArcLength = this.baseInterpolation.arcLength(0.0, 1.0), 1.0);
   }

   public Interpolation getBaseInterpolation() {
      return this.baseInterpolation;
   }

   @Override
   public Vector getPosition(double position) {
      return position > 1.0 ? null : this.baseInterpolation.getPosition(this.arcToParameter(position));
   }

   @Override
   public Vector get1stDerivative(double position) {
      return position > 1.0 ? null : this.baseInterpolation.get1stDerivative(this.arcToParameter(position)).normalize().multiply(this.totalArcLength);
   }

   @Override
   public double arcLength(double positionA, double positionB) {
      return this.baseInterpolation.arcLength(this.arcToParameter(positionA), this.arcToParameter(positionB));
   }

   private double arcToParameter(double arc) {
      if (this.cache.isEmpty()) {
         throw new IllegalStateException("Must call setNodes first.");
      } else {
         if (arc > 1.0) {
            arc = 1.0;
         }

         arc *= this.totalArcLength;
         Entry<Double, Double> floorEntry = this.cache.floorEntry(arc);
         double leftArc = floorEntry.getKey();
         double leftParameter = floorEntry.getValue();
         if (leftArc == arc) {
            return leftParameter;
         } else {
            Entry<Double, Double> ceilingEntry = this.cache.ceilingEntry(arc);
            if (ceilingEntry == null) {
               log.warning("Error in arcToParameter: no ceiling entry for " + arc + " found!");
               return 0.0;
            } else {
               double rightArc = ceilingEntry.getKey();
               double rightParameter = ceilingEntry.getValue();
               return rightArc == arc ? rightParameter : this.evaluate(arc, leftArc, leftParameter, rightArc, rightParameter);
            }
         }
      }
   }

   private double evaluate(double arc, double leftArc, double leftParameter, double rightArc, double rightParameter) {
      double midParameter = 0.0;

      for (int i = 0; i < 10; i++) {
         midParameter = (leftParameter + rightParameter) * 0.5;
         double midArc = this.baseInterpolation.arcLength(0.0, midParameter);
         this.cache.put(midArc, midParameter);
         if (midArc < leftArc) {
            return leftParameter;
         }

         if (midArc > rightArc) {
            return rightParameter;
         }

         if (Math.abs(midArc - arc) < 0.01) {
            return midParameter;
         }

         if (arc < midArc) {
            rightArc = midArc;
            rightParameter = midParameter;
         } else {
            leftArc = midArc;
            leftParameter = midParameter;
         }
      }

      return midParameter;
   }

   @Override
   public int getSegment(double position) {
      return position > 1.0 ? Integer.MAX_VALUE : this.baseInterpolation.getSegment(this.arcToParameter(position));
   }
}
