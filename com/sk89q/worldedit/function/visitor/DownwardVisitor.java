package com.sk89q.worldedit.function.visitor;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.mask.Mask;
import java.util.Collection;

public class DownwardVisitor extends RecursiveVisitor {
   private int baseY;

   public DownwardVisitor(Mask mask, RegionFunction function, int baseY) {
      super(mask, function);
      Preconditions.checkNotNull(mask);
      this.baseY = baseY;
      Collection<Vector> directions = this.getDirections();
      directions.clear();
      directions.add(new Vector(1, 0, 0));
      directions.add(new Vector(-1, 0, 0));
      directions.add(new Vector(0, 0, 1));
      directions.add(new Vector(0, 0, -1));
      directions.add(new Vector(0, -1, 0));
   }

   @Override
   protected boolean isVisitable(Vector from, Vector to) {
      int fromY = from.getBlockY();
      return (fromY == this.baseY || to.subtract(from).getBlockY() < 0) && super.isVisitable(from, to);
   }
}
