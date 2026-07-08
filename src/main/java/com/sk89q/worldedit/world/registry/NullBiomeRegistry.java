package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.biome.BiomeData;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public class NullBiomeRegistry implements BiomeRegistry {
   @Nullable
   @Override
   public BaseBiome createFromId(int id) {
      return null;
   }

   @Override
   public List<BaseBiome> getBiomes() {
      return Collections.emptyList();
   }

   @Nullable
   @Override
   public BiomeData getData(BaseBiome biome) {
      return null;
   }
}
