package com.sk89q.worldedit.function.visitor;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.RunContext;
import com.sk89q.worldedit.regions.Region;
import java.util.List;

public class RegionVisitor implements Operation {
   private final Region region;
   private final RegionFunction function;
   private int affected = 0;

   public RegionVisitor(Region region, RegionFunction function) {
      this.region = region;
      this.function = function;
   }

   public int getAffected() {
      return this.affected;
   }

   @Override
   public Operation resume(RunContext run) throws WorldEditException {
      for (Vector pt : this.region) {
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
      messages.add(this.getAffected() + " blocks affected");
   }
}
