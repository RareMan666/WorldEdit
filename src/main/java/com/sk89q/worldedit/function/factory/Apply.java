package com.sk89q.worldedit.function.factory;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.function.Contextual;
import com.sk89q.worldedit.function.EditContext;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.visitor.RegionVisitor;
import com.sk89q.worldedit.regions.NullRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.GuavaUtil;

public class Apply implements Contextual<Operation> {
   private final Region region;
   private final Contextual<? extends RegionFunction> function;

   public Apply(Contextual<? extends RegionFunction> function) {
      this(new NullRegion(), function);
   }

   public Apply(Region region, Contextual<? extends RegionFunction> function) {
      Preconditions.checkNotNull(region, "region");
      Preconditions.checkNotNull(function, "function");
      this.region = region;
      this.function = function;
   }

   public Operation createFromContext(EditContext context) {
      return new RegionVisitor(GuavaUtil.firstNonNull(context.getRegion(), this.region), this.function.createFromContext(context));
   }

   @Override
   public String toString() {
      return "set " + this.function;
   }
}
