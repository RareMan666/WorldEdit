package com.sk89q.worldedit.extent.clipboard;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.util.Location;

abstract class StoredEntity implements Entity {
   private final Location location;
   private final BaseEntity entity;

   StoredEntity(Location location, BaseEntity entity) {
      Preconditions.checkNotNull(location);
      Preconditions.checkNotNull(entity);
      this.location = location;
      this.entity = new BaseEntity(entity);
   }

   BaseEntity getEntity() {
      return this.entity;
   }

   @Override
   public BaseEntity getState() {
      return new BaseEntity(this.entity);
   }

   @Override
   public Location getLocation() {
      return this.location;
   }

   @Override
   public Extent getExtent() {
      return this.location.getExtent();
   }
}
