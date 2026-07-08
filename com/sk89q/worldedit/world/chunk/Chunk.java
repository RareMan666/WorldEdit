package com.sk89q.worldedit.world.chunk;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.DataException;

public interface Chunk {
   int getBlockID(Vector var1) throws DataException;

   int getBlockData(Vector var1) throws DataException;

   BaseBlock getBlock(Vector var1) throws DataException;
}
