package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.regions.iterator.RegionIterator;
import com.sk89q.worldedit.world.World;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class AbstractRegion implements Region {
   protected World world;

   public AbstractRegion(World world) {
      this.world = world;
   }

   @Override
   public Vector getCenter() {
      return this.getMinimumPoint().add(this.getMaximumPoint()).divide(2);
   }

   @Override
   public Iterator<BlockVector> iterator() {
      return new RegionIterator(this);
   }

   @Override
   public World getWorld() {
      return this.world;
   }

   @Override
   public void setWorld(LocalWorld world) {
      this.setWorld((World)world);
   }

   @Override
   public void setWorld(World world) {
      this.world = world;
   }

   @Override
   public void shift(Vector change) throws RegionOperationException {
      this.expand(change);
      this.contract(change);
   }

   public AbstractRegion clone() {
      try {
         return (AbstractRegion)super.clone();
      } catch (CloneNotSupportedException var2) {
         return null;
      }
   }

   @Override
   public List<BlockVector2D> polygonize(int maxPoints) {
      if (maxPoints >= 0 && maxPoints < 4) {
         throw new IllegalArgumentException("Cannot polygonize an AbstractRegion with no overridden polygonize method into less than 4 points.");
      } else {
         BlockVector min = this.getMinimumPoint().toBlockVector();
         BlockVector max = this.getMaximumPoint().toBlockVector();
         List<BlockVector2D> points = new ArrayList<>(4);
         points.add(new BlockVector2D(min.getX(), min.getZ()));
         points.add(new BlockVector2D(min.getX(), max.getZ()));
         points.add(new BlockVector2D(max.getX(), max.getZ()));
         points.add(new BlockVector2D(max.getX(), min.getZ()));
         return points;
      }
   }

   @Override
   public int getArea() {
      Vector min = this.getMinimumPoint();
      Vector max = this.getMaximumPoint();
      return (int)((max.getX() - min.getX() + 1.0) * (max.getY() - min.getY() + 1.0) * (max.getZ() - min.getZ() + 1.0));
   }

   @Override
   public int getWidth() {
      Vector min = this.getMinimumPoint();
      Vector max = this.getMaximumPoint();
      return (int)(max.getX() - min.getX() + 1.0);
   }

   @Override
   public int getHeight() {
      Vector min = this.getMinimumPoint();
      Vector max = this.getMaximumPoint();
      return (int)(max.getY() - min.getY() + 1.0);
   }

   @Override
   public int getLength() {
      Vector min = this.getMinimumPoint();
      Vector max = this.getMaximumPoint();
      return (int)(max.getZ() - min.getZ() + 1.0);
   }

   @Override
   public Set<Vector2D> getChunks() {
      Set<Vector2D> chunks = new HashSet<>();
      Vector min = this.getMinimumPoint();
      Vector max = this.getMaximumPoint();
      int minY = min.getBlockY();

      for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
         for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
            if (this.contains(new Vector(x, minY, z))) {
               chunks.add(new BlockVector2D(x >> 4, z >> 4));
            }
         }
      }

      return chunks;
   }

   @Override
   public Set<Vector> getChunkCubes() {
      Set<Vector> chunks = new HashSet<>();
      Vector min = this.getMinimumPoint();
      Vector max = this.getMaximumPoint();

      for (int x = min.getBlockX(); x <= max.getBlockX(); x++) {
         for (int y = min.getBlockY(); y <= max.getBlockY(); y++) {
            for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) {
               if (this.contains(new Vector(x, y, z))) {
                  chunks.add(new BlockVector(x >> 4, y >> 4, z >> 4));
               }
            }
         }
      }

      return chunks;
   }
}
