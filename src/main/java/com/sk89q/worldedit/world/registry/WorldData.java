package com.sk89q.worldedit.world.registry;

public interface WorldData {
   BlockRegistry getBlockRegistry();

   ItemRegistry getItemRegistry();

   EntityRegistry getEntityRegistry();

   BiomeRegistry getBiomeRegistry();
}
