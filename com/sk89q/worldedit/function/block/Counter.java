package com.sk89q.worldedit.function.block;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.RegionFunction;

public class Counter implements RegionFunction {
   private int count;

   public int getCount() {
      return this.count;
   }

   @Override
   public boolean apply(Vector position) throws WorldEditException {
      this.count++;
      return false;
   }
}
