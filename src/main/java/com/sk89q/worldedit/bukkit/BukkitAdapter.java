package com.sk89q.worldedit.bukkit;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import org.bukkit.Bukkit;

final class BukkitAdapter {
   private BukkitAdapter() {
   }

   public static BukkitWorld asBukkitWorld(World world) {
      if (world instanceof BukkitWorld) {
         return (BukkitWorld)world;
      } else {
         BukkitWorld bukkitWorld = WorldEditPlugin.getInstance().getInternalPlatform().matchWorld(world);
         if (bukkitWorld == null) {
            throw new RuntimeException("World '" + world.getName() + "' has no matching version in Bukkit");
         } else {
            return bukkitWorld;
         }
      }
   }

   public static World adapt(org.bukkit.World world) {
      Preconditions.checkNotNull(world);
      return new BukkitWorld(world);
   }

   public static org.bukkit.World adapt(World world) {
      Preconditions.checkNotNull(world);
      if (world instanceof BukkitWorld) {
         return ((BukkitWorld)world).getWorld();
      } else {
         org.bukkit.World match = Bukkit.getServer().getWorld(world.getName());
         if (match != null) {
            return match;
         } else {
            throw new IllegalArgumentException("Can't find a Bukkit world for " + world);
         }
      }
   }

   public static Location adapt(org.bukkit.Location location) {
      Preconditions.checkNotNull(location);
      Vector position = BukkitUtil.toVector(location);
      return new Location(adapt(location.getWorld()), position, location.getYaw(), location.getPitch());
   }

   public static org.bukkit.Location adapt(Location location) {
      Preconditions.checkNotNull(location);
      Vector position = location.toVector();
      return new org.bukkit.Location(
         adapt((World)location.getExtent()), position.getX(), position.getY(), position.getZ(), location.getYaw(), location.getPitch()
      );
   }

   public static org.bukkit.Location adapt(org.bukkit.World world, Vector position) {
      Preconditions.checkNotNull(world);
      Preconditions.checkNotNull(position);
      return new org.bukkit.Location(world, position.getX(), position.getY(), position.getZ());
   }

   public static org.bukkit.Location adapt(org.bukkit.World world, Location location) {
      Preconditions.checkNotNull(world);
      Preconditions.checkNotNull(location);
      return new org.bukkit.Location(world, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
   }

   public static Entity adapt(org.bukkit.entity.Entity entity) {
      Preconditions.checkNotNull(entity);
      return new BukkitEntity(entity);
   }
}
