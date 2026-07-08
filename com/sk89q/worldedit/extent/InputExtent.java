package com.sk89q.worldedit.extent;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.biome.BaseBiome;

public interface InputExtent {
   BaseBlock getBlock(Vector var1);

   BaseBlock getLazyBlock(Vector var1);

   BaseBiome getBiome(Vector2D var1);
}
