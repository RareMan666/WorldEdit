package com.sk89q.worldedit;

@Deprecated
public class WorldVectorFace extends WorldVector {
   private VectorFace face;

   public WorldVectorFace(LocalWorld world, double x, double y, double z, VectorFace face) {
      super(world, x, y, z);
      this.face = face;
   }

   public WorldVectorFace(LocalWorld world, int x, int y, int z, VectorFace face) {
      super(world, x, y, z);
      this.face = face;
   }

   public WorldVectorFace(LocalWorld world, float x, float y, float z, VectorFace face) {
      super(world, x, y, z);
      this.face = face;
   }

   public WorldVectorFace(LocalWorld world, Vector pt, VectorFace face) {
      super(world, pt);
      this.face = face;
   }

   public WorldVectorFace(LocalWorld world, VectorFace face) {
      super(world);
      this.face = face;
   }

   public VectorFace getFace() {
      return this.face;
   }

   public WorldVector getFaceVector() {
      return new WorldVector(
         this.getWorld(), this.getBlockX() - this.face.getModX(), this.getBlockY() - this.face.getModY(), this.getBlockZ() - this.face.getModZ()
      );
   }

   public static WorldVectorFace getWorldVectorFace(LocalWorld world, Vector vector, Vector face) {
      if (vector != null && face != null) {
         int x1 = vector.getBlockX();
         int y1 = vector.getBlockY();
         int z1 = vector.getBlockZ();
         int modX = x1 - face.getBlockX();
         int modY = y1 - face.getBlockY();
         int modZ = z1 - face.getBlockZ();
         byte var9;
         if (modX > 0) {
            var9 = 1;
         } else if (modX < 0) {
            var9 = -1;
         } else {
            var9 = 0;
         }

         byte var10;
         if (modY > 0) {
            var10 = 1;
         } else if (modY < 0) {
            var10 = -1;
         } else {
            var10 = 0;
         }

         byte var11;
         if (modZ > 0) {
            var11 = 1;
         } else if (modZ < 0) {
            var11 = -1;
         } else {
            var11 = 0;
         }

         return new WorldVectorFace(world, x1, y1, z1, VectorFace.fromMods(var9, var10, var11));
      } else {
         return null;
      }
   }
}
