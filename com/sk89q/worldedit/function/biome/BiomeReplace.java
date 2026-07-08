package com.sk89q.worldedit.function.biome;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.FlatRegionFunction;
import com.sk89q.worldedit.world.biome.BaseBiome;

public class BiomeReplace implements FlatRegionFunction {
   private final Extent extent;
   private BaseBiome biome;

   public BiomeReplace(Extent extent, BaseBiome biome) {
      Preconditions.checkNotNull(extent);
      Preconditions.checkNotNull(biome);
      this.extent = extent;
      this.biome = biome;
   }

   @Override
   public boolean apply(Vector2D position) throws WorldEditException {
      return this.extent.setBiome(position, this.biome);
   }
}
