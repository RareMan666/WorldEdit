package com.sk89q.worldedit;

import com.sk89q.worldedit.internal.LocalWorldAdapter;
import com.sk89q.worldedit.world.World;

@Deprecated
public class WorldVector extends Vector {
   private LocalWorld world;

   public WorldVector(LocalWorld world, double x, double y, double z) {
      super(x, y, z);
      this.world = world;
   }

   public WorldVector(LocalWorld world, int x, int y, int z) {
      super(x, y, z);
      this.world = world;
   }

   public WorldVector(LocalWorld world, float x, float y, float z) {
      super(x, y, z);
      this.world = world;
   }

   public WorldVector(LocalWorld world, Vector other) {
      super(other);
      this.world = world;
   }

   public WorldVector(LocalWorld world) {
      this.world = world;
   }

   public WorldVector(com.sk89q.worldedit.util.Location location) {
      this(LocalWorldAdapter.adapt((World)location.getExtent()), location.getX(), location.getY(), location.getZ());
   }

   public LocalWorld getWorld() {
      return this.world;
   }

   public static WorldVector toBlockPoint(LocalWorld world, double x, double y, double z) {
      return new WorldVector(world, (int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
   }

   public BlockWorldVector toWorldBlockVector() {
      return new BlockWorldVector(this);
   }

   public com.sk89q.worldedit.util.Location toLocation() {
      return new com.sk89q.worldedit.util.Location(this.getWorld(), this);
   }
}
