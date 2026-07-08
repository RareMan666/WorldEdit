package com.sk89q.worldedit.function.factory;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.NullExtent;
import com.sk89q.worldedit.function.Contextual;
import com.sk89q.worldedit.function.EditContext;
import com.sk89q.worldedit.function.GroundFunction;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.mask.NoiseFilter2D;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.visitor.LayerVisitor;
import com.sk89q.worldedit.math.noise.RandomNoise;
import com.sk89q.worldedit.regions.NullRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.Regions;
import com.sk89q.worldedit.util.GuavaUtil;

public class Paint implements Contextual<Operation> {
   private final Extent destination;
   private final Region region;
   private final Contextual<? extends RegionFunction> function;
   private final double density;

   public Paint(Contextual<? extends RegionFunction> function, double density) {
      this(new NullExtent(), new NullRegion(), function, density);
   }

   public Paint(Extent destination, Region region, Contextual<? extends RegionFunction> function, double density) {
      Preconditions.checkNotNull(destination, "destination");
      Preconditions.checkNotNull(region, "region");
      Preconditions.checkNotNull(function, "function");
      Preconditions.checkNotNull(density, "density");
      this.destination = destination;
      this.region = region;
      this.function = function;
      this.density = density;
      new NoiseFilter2D(new RandomNoise(), density);
   }

   public Operation createFromContext(EditContext context) {
      Extent destination = GuavaUtil.firstNonNull(context.getDestination(), this.destination);
      Region region = GuavaUtil.firstNonNull(context.getRegion(), this.region);
      GroundFunction ground = new GroundFunction(new ExistingBlockMask(destination), this.function.createFromContext(context));
      LayerVisitor visitor = new LayerVisitor(Regions.asFlatRegion(region), Regions.minimumBlockY(region), Regions.maximumBlockY(region), ground);
      visitor.setMask(new NoiseFilter2D(new RandomNoise(), this.density));
      return visitor;
   }

   @Override
   public String toString() {
      return "scatter " + this.function;
   }
}
