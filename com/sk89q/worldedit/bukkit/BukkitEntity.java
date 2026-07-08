package com.sk89q.worldedit.bukkit;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.entity.metadata.EntityType;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.NullWorld;
import java.lang.ref.WeakReference;
import javax.annotation.Nullable;

class BukkitEntity implements Entity {
   private final WeakReference<org.bukkit.entity.Entity> entityRef;

   BukkitEntity(org.bukkit.entity.Entity entity) {
      Preconditions.checkNotNull(entity);
      this.entityRef = new WeakReference<>(entity);
   }

   @Override
   public Extent getExtent() {
      org.bukkit.entity.Entity entity = this.entityRef.get();
      return (Extent)(entity != null ? BukkitAdapter.adapt(entity.getWorld()) : NullWorld.getInstance());
   }

   @Override
   public Location getLocation() {
      org.bukkit.entity.Entity entity = this.entityRef.get();
      return entity != null ? BukkitAdapter.adapt(entity.getLocation()) : new Location(NullWorld.getInstance());
   }

   @Override
   public BaseEntity getState() {
      org.bukkit.entity.Entity entity = this.entityRef.get();
      if (entity != null) {
         if (entity instanceof Player) {
            return null;
         } else {
            BukkitImplAdapter adapter = WorldEditPlugin.getInstance().getBukkitImplAdapter();
            return adapter != null ? adapter.getEntity(entity) : null;
         }
      } else {
         return null;
      }
   }

   @Override
   public boolean remove() {
      org.bukkit.entity.Entity entity = this.entityRef.get();
      if (entity != null) {
         entity.remove();
         return entity.isDead();
      } else {
         return true;
      }
   }

   @Nullable
   @Override
   public <T> T getFacet(Class<? extends T> cls) {
      org.bukkit.entity.Entity entity = this.entityRef.get();
      return (T)(entity != null && EntityType.class.isAssignableFrom(cls) ? new BukkitEntityType(entity) : null);
   }
}
