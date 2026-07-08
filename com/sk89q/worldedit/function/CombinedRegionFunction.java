package com.sk89q.worldedit.function;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CombinedRegionFunction implements RegionFunction {
   private final List<RegionFunction> functions = new ArrayList<>();

   public CombinedRegionFunction() {
   }

   public CombinedRegionFunction(Collection<RegionFunction> functions) {
      Preconditions.checkNotNull(functions);
      this.functions.addAll(functions);
   }

   public CombinedRegionFunction(RegionFunction... function) {
      this(Arrays.asList(Preconditions.checkNotNull(function)));
   }

   public void add(Collection<RegionFunction> functions) {
      Preconditions.checkNotNull(functions);
      this.functions.addAll(functions);
   }

   public void add(RegionFunction... function) {
      this.add(Arrays.asList(Preconditions.checkNotNull(function)));
   }

   @Override
   public boolean apply(Vector position) throws WorldEditException {
      boolean ret = false;

      for (RegionFunction function : this.functions) {
         if (function.apply(position)) {
            ret = true;
         }
      }

      return ret;
   }
}
