package com.sk89q.worldedit.function.block;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.pattern.Pattern;

public class BlockReplace implements RegionFunction {
   private final Extent extent;
   private Pattern pattern;

   public BlockReplace(Extent extent, Pattern pattern) {
      Preconditions.checkNotNull(extent);
      Preconditions.checkNotNull(pattern);
      this.extent = extent;
      this.pattern = pattern;
   }

   @Override
   public boolean apply(Vector position) throws WorldEditException {
      return this.extent.setBlock(position, this.pattern.apply(position));
   }
}
