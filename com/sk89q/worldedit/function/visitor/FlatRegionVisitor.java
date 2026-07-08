package com.sk89q.worldedit.function.visitor;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.FlatRegionFunction;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.RunContext;
import com.sk89q.worldedit.regions.FlatRegion;
import java.util.List;

public class FlatRegionVisitor implements Operation {
   private final FlatRegion flatRegion;
   private final FlatRegionFunction function;
   private int affected = 0;

   public FlatRegionVisitor(FlatRegion flatRegion, FlatRegionFunction function) {
      Preconditions.checkNotNull(flatRegion);
      Preconditions.checkNotNull(function);
      this.flatRegion = flatRegion;
      this.function = function;
   }

   public int getAffected() {
      return this.affected;
   }

   @Override
   public Operation resume(RunContext run) throws WorldEditException {
      for (Vector2D pt : this.flatRegion.asFlatRegion()) {
         if (this.function.apply(pt)) {
            this.affected++;
         }
      }

      return null;
   }

   @Override
   public void cancel() {
   }

   @Override
   public void addStatusMessages(List<String> messages) {
      messages.add(this.getAffected() + " columns affected");
   }
}
