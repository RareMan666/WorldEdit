package com.sk89q.worldedit.function.visitor;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.LayerFunction;
import com.sk89q.worldedit.function.mask.Mask2D;
import com.sk89q.worldedit.function.mask.Masks;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.RunContext;
import com.sk89q.worldedit.regions.FlatRegion;
import java.util.List;

public class LayerVisitor implements Operation {
   private final FlatRegion flatRegion;
   private final LayerFunction function;
   private Mask2D mask = Masks.alwaysTrue2D();
   private int minY;
   private int maxY;

   public LayerVisitor(FlatRegion flatRegion, int minY, int maxY, LayerFunction function) {
      Preconditions.checkNotNull(flatRegion);
      Preconditions.checkArgument(minY <= maxY, "minY <= maxY required");
      Preconditions.checkNotNull(function);
      this.flatRegion = flatRegion;
      this.minY = minY;
      this.maxY = maxY;
      this.function = function;
   }

   public Mask2D getMask() {
      return this.mask;
   }

   public void setMask(Mask2D mask) {
      Preconditions.checkNotNull(mask);
      this.mask = mask;
   }

   @Override
   public Operation resume(RunContext run) throws WorldEditException {
      for (Vector2D column : this.flatRegion.asFlatRegion()) {
         if (this.mask.test(column)) {
            if (this.function.isGround(column.toVector(this.maxY + 1))) {
               return null;
            }

            boolean found = false;
            int groundY = 0;

            for (int y = this.maxY; y >= this.minY; y--) {
               Vector test = column.toVector(y);
               if (!found && this.function.isGround(test)) {
                  found = true;
                  groundY = y;
               }

               if (found && !this.function.apply(test, groundY - y)) {
                  break;
               }
            }
         }
      }

      return null;
   }

   @Override
   public void cancel() {
   }

   @Override
   public void addStatusMessages(List<String> messages) {
   }
}
