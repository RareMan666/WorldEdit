package com.sk89q.worldedit.regions.shape;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.regions.Region;

public class RegionShape extends ArbitraryShape {
   public RegionShape(Region extent) {
      super(extent);
   }

   @Override
   protected BaseBlock getMaterial(int x, int y, int z, BaseBlock defaultMaterial) {
      return !this.extent.contains(new Vector(x, y, z)) ? null : defaultMaterial;
   }
}
