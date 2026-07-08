package com.sk89q.worldedit.extent;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.OperationQueue;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.biome.BaseBiome;
import java.util.List;
import javax.annotation.Nullable;

public abstract class AbstractDelegateExtent implements Extent {
   private final Extent extent;

   protected AbstractDelegateExtent(Extent extent) {
      Preconditions.checkNotNull(extent);
      this.extent = extent;
   }

   public Extent getExtent() {
      return this.extent;
   }

   @Override
   public BaseBlock getBlock(Vector position) {
      return this.extent.getBlock(position);
   }

   @Override
   public BaseBlock getLazyBlock(Vector position) {
      return this.extent.getLazyBlock(position);
   }

   @Override
   public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
      return this.extent.setBlock(location, block);
   }

   @Nullable
   @Override
   public Entity createEntity(Location location, BaseEntity entity) {
      return this.extent.createEntity(location, entity);
   }

   @Override
   public List<? extends Entity> getEntities() {
      return this.extent.getEntities();
   }

   @Override
   public List<? extends Entity> getEntities(Region region) {
      return this.extent.getEntities(region);
   }

   @Override
   public BaseBiome getBiome(Vector2D position) {
      return this.extent.getBiome(position);
   }

   @Override
   public boolean setBiome(Vector2D position, BaseBiome biome) {
      return this.extent.setBiome(position, biome);
   }

   @Override
   public Vector getMinimumPoint() {
      return this.extent.getMinimumPoint();
   }

   @Override
   public Vector getMaximumPoint() {
      return this.extent.getMaximumPoint();
   }

   protected Operation commitBefore() {
      return null;
   }

   @Nullable
   @Override
   public final Operation commit() {
      Operation ours = this.commitBefore();
      Operation other = this.extent.commit();
      if (ours != null && other != null) {
         return new OperationQueue(ours, other);
      } else if (ours != null) {
         return ours;
      } else {
         return other != null ? other : null;
      }
   }
}
