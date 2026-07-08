package com.sk89q.worldedit.world.biome;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.sk89q.worldedit.world.registry.BiomeRegistry;
import javax.annotation.Nullable;

class BiomeName implements Function<BaseBiome, String> {
   private final BiomeRegistry registry;

   BiomeName(BiomeRegistry registry) {
      Preconditions.checkNotNull(registry);
      this.registry = registry;
   }

   @Nullable
   public String apply(BaseBiome input) {
      BiomeData data = this.registry.getData(input);
      return data != null ? data.getName() : null;
   }
}
