package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.biome.BiomeData;
import java.util.List;
import javax.annotation.Nullable;

public interface BiomeRegistry {
   @Nullable
   BaseBiome createFromId(int var1);

   List<BaseBiome> getBiomes();

   @Nullable
   BiomeData getData(BaseBiome var1);
}
