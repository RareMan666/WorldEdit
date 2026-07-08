package com.sk89q.worldedit.util;

import com.sk89q.worldedit.Vector;
import javax.annotation.Nullable;

public enum Direction {
   NORTH(new Vector(0, 0, -1), Direction.Flag.CARDINAL),
   EAST(new Vector(1, 0, 0), Direction.Flag.CARDINAL),
   SOUTH(new Vector(0, 0, 1), Direction.Flag.CARDINAL),
   WEST(new Vector(-1, 0, 0), Direction.Flag.CARDINAL),
   UP(new Vector(0, 1, 0), Direction.Flag.UPRIGHT),
   DOWN(new Vector(0, -1, 0), Direction.Flag.UPRIGHT),
   NORTHEAST(new Vector(1, 0, -1), Direction.Flag.ORDINAL),
   NORTHWEST(new Vector(-1, 0, -1), Direction.Flag.ORDINAL),
   SOUTHEAST(new Vector(1, 0, 1), Direction.Flag.ORDINAL),
   SOUTHWEST(new Vector(-1, 0, 1), Direction.Flag.ORDINAL),
   WEST_NORTHWEST(new Vector(-Math.cos(Math.PI / 8), 0.0, -Math.sin(Math.PI / 8)), Direction.Flag.SECONDARY_ORDINAL),
   WEST_SOUTHWEST(new Vector(-Math.cos(Math.PI / 8), 0.0, Math.sin(Math.PI / 8)), Direction.Flag.SECONDARY_ORDINAL),
   NORTH_NORTHWEST(new Vector(-Math.sin(Math.PI / 8), 0.0, -Math.cos(Math.PI / 8)), Direction.Flag.SECONDARY_ORDINAL),
   NORTH_NORTHEAST(new Vector(Math.sin(Math.PI / 8), 0.0, -Math.cos(Math.PI / 8)), Direction.Flag.SECONDARY_ORDINAL),
   EAST_NORTHEAST(new Vector(Math.cos(Math.PI / 8), 0.0, -Math.sin(Math.PI / 8)), Direction.Flag.SECONDARY_ORDINAL),
   EAST_SOUTHEAST(new Vector(Math.cos(Math.PI / 8), 0.0, Math.sin(Math.PI / 8)), Direction.Flag.SECONDARY_ORDINAL),
   SOUTH_SOUTHEAST(new Vector(Math.sin(Math.PI / 8), 0.0, Math.cos(Math.PI / 8)), Direction.Flag.SECONDARY_ORDINAL),
   SOUTH_SOUTHWEST(new Vector(-Math.sin(Math.PI / 8), 0.0, Math.cos(Math.PI / 8)), Direction.Flag.SECONDARY_ORDINAL);

   private final Vector direction;
   private final int flags;

   private Direction(Vector vector, int flags) {
      this.direction = vector.normalize();
      this.flags = flags;
   }

   public boolean isCardinal() {
      return (this.flags & Direction.Flag.CARDINAL) > 0;
   }

   public boolean isOrdinal() {
      return (this.flags & Direction.Flag.ORDINAL) > 0;
   }

   public boolean isSecondaryOrdinal() {
      return (this.flags & Direction.Flag.SECONDARY_ORDINAL) > 0;
   }

   public boolean isUpright() {
      return (this.flags & Direction.Flag.UPRIGHT) > 0;
   }

   public Vector toVector() {
      return this.direction;
   }

   @Nullable
   public static Direction findClosest(Vector vector, int flags) {
      if ((flags & Direction.Flag.UPRIGHT) == 0) {
         vector = vector.setY(0);
      }

      vector = vector.normalize();
      Direction closest = null;
      double closestDot = -2.0;

      for (Direction direction : values()) {
         if ((~flags & direction.flags) <= 0) {
            double dot = direction.toVector().dot(vector);
            if (dot >= closestDot) {
               closest = direction;
               closestDot = dot;
            }
         }
      }

      return closest;
   }

   public static final class Flag {
      public static int CARDINAL = 1;
      public static int ORDINAL = 2;
      public static int SECONDARY_ORDINAL = 4;
      public static int UPRIGHT = 8;
      public static int ALL = CARDINAL | ORDINAL | SECONDARY_ORDINAL | UPRIGHT;

      private Flag() {
      }
   }
}
