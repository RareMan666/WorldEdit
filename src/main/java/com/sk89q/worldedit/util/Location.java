package com.sk89q.worldedit.util;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.Extent;

public class Location {
   private final Extent extent;
   private final Vector position;
   private final float pitch;
   private final float yaw;

   public Location(Extent extent) {
      this(extent, new Vector(), new Vector());
   }

   public Location(Extent extent, double x, double y, double z) {
      this(extent, new Vector(x, y, z), new Vector());
   }

   public Location(Extent extent, Vector position) {
      this(extent, position, new Vector());
   }

   public Location(Extent extent, double x, double y, double z, Vector direction) {
      this(extent, new Vector(x, y, z), direction);
   }

   public Location(Extent extent, double x, double y, double z, float yaw, float pitch) {
      this(extent, new Vector(x, y, z), yaw, pitch);
   }

   public Location(Extent extent, Vector position, Vector direction) {
      this(extent, position, direction.toYaw(), direction.toPitch());
   }

   public Location(Extent extent, Vector position, float yaw, float pitch) {
      Preconditions.checkNotNull(extent);
      Preconditions.checkNotNull(position);
      this.extent = extent;
      this.position = position;
      this.pitch = pitch;
      this.yaw = yaw;
   }

   public Extent getExtent() {
      return this.extent;
   }

   public Location setExtent(Extent extent) {
      return new Location(extent, this.position, this.getDirection());
   }

   public float getYaw() {
      return this.yaw;
   }

   public Location setYaw(float yaw) {
      return new Location(this.extent, this.position, yaw, this.pitch);
   }

   public float getPitch() {
      return this.pitch;
   }

   public Location setPitch(float pitch) {
      return new Location(this.extent, this.position, this.yaw, pitch);
   }

   public Location setDirection(float yaw, float pitch) {
      return new Location(this.extent, this.position, yaw, pitch);
   }

   public Vector getDirection() {
      double yaw = Math.toRadians(this.getYaw());
      double pitch = Math.toRadians(this.getPitch());
      double xz = Math.cos(pitch);
      return new Vector(-xz * Math.sin(yaw), -Math.sin(pitch), xz * Math.cos(yaw));
   }

   public Location setDirection(Vector direction) {
      return new Location(this.extent, this.position, direction.toYaw(), direction.toPitch());
   }

   public Vector toVector() {
      return this.position;
   }

   public double getX() {
      return this.position.getX();
   }

   public int getBlockX() {
      return this.position.getBlockX();
   }

   public Location setX(double x) {
      return new Location(this.extent, this.position.setX(x), this.yaw, this.pitch);
   }

   public Location setX(int x) {
      return new Location(this.extent, this.position.setX(x), this.yaw, this.pitch);
   }

   public double getY() {
      return this.position.getY();
   }

   public int getBlockY() {
      return this.position.getBlockY();
   }

   public Location setY(double y) {
      return new Location(this.extent, this.position.setY(y), this.yaw, this.pitch);
   }

   public Location setY(int y) {
      return new Location(this.extent, this.position.setY(y), this.yaw, this.pitch);
   }

   public double getZ() {
      return this.position.getZ();
   }

   public int getBlockZ() {
      return this.position.getBlockZ();
   }

   public Location setZ(double z) {
      return new Location(this.extent, this.position.setZ(z), this.yaw, this.pitch);
   }

   public Location setZ(int z) {
      return new Location(this.extent, this.position.setZ(z), this.yaw, this.pitch);
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Location location = (Location)o;
         if (Double.doubleToLongBits(this.pitch) != Double.doubleToLongBits(location.pitch)) {
            return false;
         } else if (Double.doubleToLongBits(this.yaw) != Double.doubleToLongBits(location.yaw)) {
            return false;
         } else {
            return !this.position.equals(location.position) ? false : this.extent.equals(location.extent);
         }
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      int result = this.extent.hashCode();
      result = 31 * result + this.position.hashCode();
      result = 31 * result + Float.floatToIntBits(this.pitch);
      return 31 * result + Float.floatToIntBits(this.yaw);
   }
}
