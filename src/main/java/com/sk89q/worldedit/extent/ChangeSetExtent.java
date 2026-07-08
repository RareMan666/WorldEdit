package com.sk89q.worldedit.extent;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.history.change.BlockChange;
import com.sk89q.worldedit.history.change.EntityCreate;
import com.sk89q.worldedit.history.change.EntityRemove;
import com.sk89q.worldedit.history.changeset.ChangeSet;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class ChangeSetExtent extends AbstractDelegateExtent {
   private final ChangeSet changeSet;

   public ChangeSetExtent(Extent extent, ChangeSet changeSet) {
      super(extent);
      Preconditions.checkNotNull(changeSet);
      this.changeSet = changeSet;
   }

   @Override
   public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
      BaseBlock previous = this.getBlock(location);
      this.changeSet.add(new BlockChange(location.toBlockVector(), previous, block));
      return super.setBlock(location, block);
   }

   @Nullable
   @Override
   public Entity createEntity(Location location, BaseEntity state) {
      Entity entity = super.createEntity(location, state);
      if (entity != null) {
         this.changeSet.add(new EntityCreate(location, state, entity));
      }

      return entity;
   }

   @Override
   public List<? extends Entity> getEntities() {
      return this.wrapEntities(super.getEntities());
   }

   @Override
   public List<? extends Entity> getEntities(Region region) {
      return this.wrapEntities(super.getEntities(region));
   }

   private List<? extends Entity> wrapEntities(List<? extends Entity> entities) {
      List<Entity> newList = new ArrayList<>(entities.size());

      for (Entity entity : entities) {
         newList.add(new ChangeSetExtent.TrackedEntity(entity));
      }

      return newList;
   }

   private class TrackedEntity implements Entity {
      private final Entity entity;

      private TrackedEntity(Entity entity) {
         this.entity = entity;
      }

      @Override
      public BaseEntity getState() {
         return this.entity.getState();
      }

      @Override
      public Location getLocation() {
         return this.entity.getLocation();
      }

      @Override
      public Extent getExtent() {
         return this.entity.getExtent();
      }

      @Override
      public boolean remove() {
         Location location = this.entity.getLocation();
         BaseEntity state = this.entity.getState();
         boolean success = this.entity.remove();
         if (state != null && success) {
            ChangeSetExtent.this.changeSet.add(new EntityRemove(location, state));
         }

         return success;
      }

      @Nullable
      @Override
      public <T> T getFacet(Class<? extends T> cls) {
         return this.entity.getFacet(cls);
      }
   }
}
