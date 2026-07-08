package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.world.World;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

public class NullRegion implements Region {
   private World world;

   @Override
   public Vector getMinimumPoint() {
      return new Vector(0, 0, 0);
   }

   @Override
   public Vector getMaximumPoint() {
      return new Vector(0, 0, 0);
   }

   @Override
   public Vector getCenter() {
      return new Vector(0, 0, 0);
   }

   @Override
   public int getArea() {
      return 0;
   }

   @Override
   public int getWidth() {
      return 0;
   }

   @Override
   public int getHeight() {
      return 0;
   }

   @Override
   public int getLength() {
      return 0;
   }

   @Override
   public void expand(Vector... changes) throws RegionOperationException {
      throw new RegionOperationException("Cannot change NullRegion");
   }

   @Override
   public void contract(Vector... changes) throws RegionOperationException {
      throw new RegionOperationException("Cannot change NullRegion");
   }

   @Override
   public void shift(Vector change) throws RegionOperationException {
      throw new RegionOperationException("Cannot change NullRegion");
   }

   @Override
   public boolean contains(Vector position) {
      return false;
   }

   @Override
   public Set<Vector2D> getChunks() {
      return Collections.emptySet();
   }

   @Override
   public Set<Vector> getChunkCubes() {
      return Collections.emptySet();
   }

   @Override
   public World getWorld() {
      return this.world;
   }

   @Override
   public void setWorld(World world) {
      this.world = world;
   }

   @Override
   public void setWorld(LocalWorld world) {
      this.setWorld((World)world);
   }

   public NullRegion clone() {
      return new NullRegion();
   }

   @Override
   public List<BlockVector2D> polygonize(int maxPoints) {
      return Collections.emptyList();
   }

   @Override
   public Iterator<BlockVector> iterator() {
      return new Iterator<BlockVector>() {
         @Override
         public boolean hasNext() {
            return false;
         }

         public BlockVector next() {
            throw new NoSuchElementException();
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException("Cannot remove");
         }
      };
   }
}
