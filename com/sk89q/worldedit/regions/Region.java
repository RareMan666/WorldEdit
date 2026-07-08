package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.world.World;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public interface Region extends Iterable<BlockVector>, Cloneable {
   Vector getMinimumPoint();

   Vector getMaximumPoint();

   Vector getCenter();

   int getArea();

   int getWidth();

   int getHeight();

   int getLength();

   void expand(Vector... var1) throws RegionOperationException;

   void contract(Vector... var1) throws RegionOperationException;

   void shift(Vector var1) throws RegionOperationException;

   boolean contains(Vector var1);

   Set<Vector2D> getChunks();

   Set<Vector> getChunkCubes();

   @Nullable
   World getWorld();

   void setWorld(@Nullable World var1);

   @Deprecated
   void setWorld(@Nullable LocalWorld var1);

   Region clone();

   List<BlockVector2D> polygonize(int var1);
}
