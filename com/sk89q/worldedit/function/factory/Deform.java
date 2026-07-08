package com.sk89q.worldedit.function.factory;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.NullExtent;
import com.sk89q.worldedit.function.Contextual;
import com.sk89q.worldedit.function.EditContext;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.RunContext;
import com.sk89q.worldedit.internal.expression.ExpressionException;
import com.sk89q.worldedit.regions.NullRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.GuavaUtil;
import java.util.List;

public class Deform implements Contextual<Operation> {
   private Extent destination;
   private Region region;
   private String expression;
   private Deform.Mode mode = Deform.Mode.UNIT_CUBE;
   private Vector offset = new Vector();

   public Deform(String expression) {
      this(new NullExtent(), new NullRegion(), expression);
   }

   public Deform(String expression, Deform.Mode mode) {
      this(new NullExtent(), new NullRegion(), expression, mode);
   }

   public Deform(Extent destination, Region region, String expression) {
      this(destination, region, expression, Deform.Mode.UNIT_CUBE);
   }

   public Deform(Extent destination, Region region, String expression, Deform.Mode mode) {
      Preconditions.checkNotNull(destination, "destination");
      Preconditions.checkNotNull(region, "region");
      Preconditions.checkNotNull(expression, "expression");
      Preconditions.checkNotNull(mode, "mode");
      this.destination = destination;
      this.region = region;
      this.expression = expression;
      this.mode = mode;
   }

   public Extent getDestination() {
      return this.destination;
   }

   public void setDestination(Extent destination) {
      Preconditions.checkNotNull(destination, "destination");
      this.destination = destination;
   }

   public Region getRegion() {
      return this.region;
   }

   public void setRegion(Region region) {
      Preconditions.checkNotNull(region, "region");
      this.region = region;
   }

   public String getExpression() {
      return this.expression;
   }

   public void setExpression(String expression) {
      Preconditions.checkNotNull(expression, "expression");
      this.expression = expression;
   }

   public Deform.Mode getMode() {
      return this.mode;
   }

   public void setMode(Deform.Mode mode) {
      Preconditions.checkNotNull(mode, "mode");
      this.mode = mode;
   }

   public Vector getOffset() {
      return this.offset;
   }

   public void setOffset(Vector offset) {
      Preconditions.checkNotNull(offset, "offset");
      this.offset = offset;
   }

   @Override
   public String toString() {
      return "deformation of " + this.expression;
   }

   public Operation createFromContext(EditContext context) {
      Region region = GuavaUtil.firstNonNull(context.getRegion(), this.region);
      Vector zero;
      Vector unit;
      switch (this.mode) {
         case UNIT_CUBE:
            Vector min = region.getMinimumPoint();
            Vector max = region.getMaximumPoint();
            zero = max.add(min).multiply(0.5);
            unit = max.subtract(zero);
            if (unit.getX() == 0.0) {
               unit = unit.setX(1.0);
            }

            if (unit.getY() == 0.0) {
               unit = unit.setY(1.0);
            }

            if (unit.getZ() == 0.0) {
               unit = unit.setZ(1.0);
            }
            break;
         case RAW_COORD:
            zero = Vector.ZERO;
            unit = Vector.ONE;
            break;
         case OFFSET:
         default:
            zero = this.offset;
            unit = Vector.ONE;
      }

      return new Deform.DeformOperation(context.getDestination(), region, zero, unit, this.expression);
   }

   private static final class DeformOperation implements Operation {
      private final Extent destination;
      private final Region region;
      private final Vector zero;
      private final Vector unit;
      private final String expression;

      private DeformOperation(Extent destination, Region region, Vector zero, Vector unit, String expression) {
         this.destination = destination;
         this.region = region;
         this.zero = zero;
         this.unit = unit;
         this.expression = expression;
      }

      @Override
      public Operation resume(RunContext run) throws WorldEditException {
         try {
            ((EditSession)this.destination).deformRegion(this.region, this.zero, this.unit, this.expression);
            return null;
         } catch (ExpressionException var3) {
            throw new RuntimeException("Failed to execute expression", var3);
         }
      }

      @Override
      public void cancel() {
      }

      @Override
      public void addStatusMessages(List<String> messages) {
         messages.add("deformed using " + this.expression);
      }
   }

   public static enum Mode {
      RAW_COORD,
      OFFSET,
      UNIT_CUBE;
   }
}
