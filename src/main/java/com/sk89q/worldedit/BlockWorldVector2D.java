package com.sk89q.worldedit;

@Deprecated
public class BlockWorldVector2D extends WorldVector2D {
   public BlockWorldVector2D(LocalWorld world, double x, double z) {
      super(world, x, z);
   }

   public BlockWorldVector2D(LocalWorld world, float x, float z) {
      super(world, x, z);
   }

   public BlockWorldVector2D(LocalWorld world, int x, int z) {
      super(world, x, z);
   }

   public BlockWorldVector2D(LocalWorld world, Vector2D position) {
      super(world, position);
   }

   public BlockWorldVector2D(LocalWorld world) {
      super(world);
   }

   @Override
   public boolean equals(Object obj) {
      if (!(obj instanceof WorldVector2D)) {
         return false;
      } else {
         WorldVector2D other = (WorldVector2D)obj;
         return other.getWorld().equals(this.world) && (int)other.getX() == (int)this.x && (int)other.getZ() == (int)this.z;
      }
   }

   @Override
   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.world.hashCode();
      long temp = Double.doubleToLongBits(this.x);
      result = 31 * result + (int)(temp ^ temp >>> 32);
      temp = Double.doubleToLongBits(this.z);
      return 31 * result + (int)(temp ^ temp >>> 32);
   }
}
