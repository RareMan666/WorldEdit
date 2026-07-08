package com.sk89q.worldedit.session.request;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.regions.NullRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionOperationException;
import com.sk89q.worldedit.world.World;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class RequestSelection implements Region {
   protected Region getRegion() {
      LocalSession session = Request.request().getSession();
      World world = Request.request().getWorld();
      if (session != null && world != null) {
         try {
            return session.getSelection(world);
         } catch (IncompleteRegionException var4) {
         }
      }

      return new NullRegion();
   }

   @Override
   public Vector getMinimumPoint() {
      return this.getRegion().getMinimumPoint();
   }

   @Override
   public Vector getMaximumPoint() {
      return this.getRegion().getMaximumPoint();
   }

   @Override
   public Vector getCenter() {
      return this.getRegion().getCenter();
   }

   @Override
   public int getArea() {
      return this.getRegion().getArea();
   }

   @Override
   public int getWidth() {
      return this.getRegion().getWidth();
   }

   @Override
   public int getHeight() {
      return this.getRegion().getHeight();
   }

   @Override
   public int getLength() {
      return this.getRegion().getLength();
   }

   @Override
   public void expand(Vector... changes) throws RegionOperationException {
      this.getRegion().expand(changes);
   }

   @Override
   public void contract(Vector... changes) throws RegionOperationException {
      this.getRegion().contract(changes);
   }

   @Override
   public void shift(Vector change) throws RegionOperationException {
      this.getRegion().shift(change);
   }

   @Override
   public boolean contains(Vector position) {
      return this.getRegion().contains(position);
   }

   @Override
   public Set<Vector2D> getChunks() {
      return this.getRegion().getChunks();
   }

   @Override
   public Set<Vector> getChunkCubes() {
      return this.getRegion().getChunkCubes();
   }

   @Override
   public World getWorld() {
      return this.getRegion().getWorld();
   }

   @Override
   public void setWorld(LocalWorld world) {
      this.setWorld((World)world);
   }

   @Override
   public void setWorld(World world) {
      this.getRegion().setWorld(world);
   }

   @Override
   public Region clone() {
      return this;
   }

   @Override
   public List<BlockVector2D> polygonize(int maxPoints) {
      return this.getRegion().polygonize(maxPoints);
   }

   @Override
   public Iterator<BlockVector> iterator() {
      return this.getRegion().iterator();
   }
}
