package com.sk89q.worldedit.bukkit.adapter;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.BaseEntity;
import javax.annotation.Nullable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;

public interface BukkitImplAdapter {
   int getBlockId(Material var1);

   Material getMaterial(int var1);

   int getBiomeId(Biome var1);

   Biome getBiome(int var1);

   BaseBlock getBlock(Location var1);

   boolean setBlock(Location var1, BaseBlock var2, boolean var3);

   @Nullable
   BaseEntity getEntity(Entity var1);

   @Nullable
   Entity createEntity(Location var1, BaseEntity var2);
}
