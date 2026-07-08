package com.sk89q.worldedit.bukkit;

import com.sk89q.worldedit.world.registry.BiomeRegistry;
import com.sk89q.worldedit.world.registry.LegacyWorldData;

class BukkitWorldData extends LegacyWorldData {
   private static final BukkitWorldData INSTANCE = new BukkitWorldData();
   private final BiomeRegistry biomeRegistry = new BukkitBiomeRegistry();

   @Override
   public BiomeRegistry getBiomeRegistry() {
      return this.biomeRegistry;
   }

   public static BukkitWorldData getInstance() {
      return INSTANCE;
   }
}
