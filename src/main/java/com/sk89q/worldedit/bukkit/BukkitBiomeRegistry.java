package com.sk89q.worldedit.bukkit;

import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.biome.BiomeData;
import com.sk89q.worldedit.world.registry.BiomeRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import org.bukkit.block.Biome;

class BukkitBiomeRegistry implements BiomeRegistry {
   @Nullable
   @Override
   public BaseBiome createFromId(int id) {
      return new BaseBiome(id);
   }

   @Override
   public List<BaseBiome> getBiomes() {
      BukkitImplAdapter adapter = WorldEditPlugin.getInstance().getBukkitImplAdapter();
      if (adapter == null) {
         return Collections.emptyList();
      } else {
         List<BaseBiome> biomes = new ArrayList<>();

         for (Biome biome : Biome.values()) {
            int biomeId = adapter.getBiomeId(biome);
            biomes.add(new BaseBiome(biomeId));
         }

         return biomes;
      }
   }

   @Nullable
   @Override
   public BiomeData getData(BaseBiome biome) {
      BukkitImplAdapter adapter = WorldEditPlugin.getInstance().getBukkitImplAdapter();
      if (adapter != null) {
         final Biome bukkitBiome = adapter.getBiome(biome.getId());
         return new BiomeData() {
            @Override
            public String getName() {
               return bukkitBiome.name();
            }
         };
      } else {
         return null;
      }
   }
}
