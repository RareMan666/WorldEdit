package com.sk89q.worldedit.function.visitor;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.mask.Mask;
import java.util.Collection;

public class NonRisingVisitor extends RecursiveVisitor {
   public NonRisingVisitor(Mask mask, RegionFunction function) {
      super(mask, function);
      Collection<Vector> directions = this.getDirections();
      directions.clear();
      directions.add(new Vector(1, 0, 0));
      directions.add(new Vector(-1, 0, 0));
      directions.add(new Vector(0, 0, 1));
      directions.add(new Vector(0, 0, -1));
      directions.add(new Vector(0, -1, 0));
   }
}
