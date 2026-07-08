package com.sk89q.worldedit.extent;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.biome.BaseBiome;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public class NullExtent implements Extent {
   private final Vector nullPoint = new Vector(0, 0, 0);

   @Override
   public Vector getMinimumPoint() {
      return this.nullPoint;
   }

   @Override
   public Vector getMaximumPoint() {
      return this.nullPoint;
   }

   @Override
   public List<Entity> getEntities(Region region) {
      return Collections.emptyList();
   }

   @Override
   public List<Entity> getEntities() {
      return Collections.emptyList();
   }

   @Nullable
   @Override
   public Entity createEntity(Location location, BaseEntity entity) {
      return null;
   }

   @Override
   public BaseBlock getBlock(Vector position) {
      return new BaseBlock(0);
   }

   @Override
   public BaseBlock getLazyBlock(Vector position) {
      return new BaseBlock(0);
   }

   @Nullable
   @Override
   public BaseBiome getBiome(Vector2D position) {
      return null;
   }

   @Override
   public boolean setBlock(Vector position, BaseBlock block) throws WorldEditException {
      return false;
   }

   @Override
   public boolean setBiome(Vector2D position, BaseBiome biome) {
      return false;
   }

   @Nullable
   @Override
   public Operation commit() {
      return null;
   }
}
