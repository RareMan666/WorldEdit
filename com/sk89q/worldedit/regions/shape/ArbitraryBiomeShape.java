package com.sk89q.worldedit.regions.shape;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.FlatRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.biome.BaseBiome;

public abstract class ArbitraryBiomeShape {
   private final FlatRegion extent;
   private int cacheOffsetX;
   private int cacheOffsetZ;
   private int cacheSizeX;
   private int cacheSizeZ;
   private final BaseBiome[] cache;
   private static final BaseBiome OUTSIDE = new BaseBiome(0) {
      @Override
      public int hashCode() {
         return 0;
      }

      @Override
      public boolean equals(Object o) {
         return this == o;
      }
   };

   public ArbitraryBiomeShape(Region extent) {
      if (extent instanceof FlatRegion) {
         this.extent = (FlatRegion)extent;
      } else {
         this.extent = new CuboidRegion(extent.getWorld(), extent.getMinimumPoint(), extent.getMaximumPoint());
      }

      Vector2D min = extent.getMinimumPoint().toVector2D();
      Vector2D max = extent.getMaximumPoint().toVector2D();
      this.cacheOffsetX = min.getBlockX() - 1;
      this.cacheOffsetZ = min.getBlockZ() - 1;
      this.cacheSizeX = (int)(max.getX() - this.cacheOffsetX + 2.0);
      this.cacheSizeZ = (int)(max.getZ() - this.cacheOffsetZ + 2.0);
      this.cache = new BaseBiome[this.cacheSizeX * this.cacheSizeZ];
   }

   protected Iterable<Vector2D> getExtent() {
      return this.extent.asFlatRegion();
   }

   protected abstract BaseBiome getBiome(int var1, int var2, BaseBiome var3);

   private BaseBiome getBiomeCached(int x, int z, BaseBiome baseBiome) {
      int index = z - this.cacheOffsetZ + (x - this.cacheOffsetX) * this.cacheSizeZ;
      BaseBiome cacheEntry = this.cache[index];
      if (cacheEntry == null) {
         BaseBiome material = this.getBiome(x, z, baseBiome);
         if (material == null) {
            this.cache[index] = OUTSIDE;
            return null;
         } else {
            this.cache[index] = material;
            return material;
         }
      } else {
         return cacheEntry == OUTSIDE ? null : cacheEntry;
      }
   }

   private boolean isInsideCached(int x, int z, BaseBiome baseBiome) {
      int index = z - this.cacheOffsetZ + (x - this.cacheOffsetX) * this.cacheSizeZ;
      BaseBiome cacheEntry = this.cache[index];
      return cacheEntry == null ? this.getBiomeCached(x, z, baseBiome) != null : cacheEntry != OUTSIDE;
   }

   public int generate(EditSession editSession, BaseBiome baseBiome, boolean hollow) {
      int affected = 0;

      for (Vector2D position : this.getExtent()) {
         int x = position.getBlockX();
         int z = position.getBlockZ();
         if (!hollow) {
            BaseBiome material = this.getBiome(x, z, baseBiome);
            if (material != null && material != OUTSIDE) {
               editSession.getWorld().setBiome(position, material);
               affected++;
            }
         } else {
            BaseBiome material = this.getBiomeCached(x, z, baseBiome);
            if (material != null) {
               boolean draw = false;
               if (!this.isInsideCached(x + 1, z, baseBiome)) {
                  draw = true;
               } else if (!this.isInsideCached(x - 1, z, baseBiome)) {
                  draw = true;
               } else if (!this.isInsideCached(x, z + 1, baseBiome)) {
                  draw = true;
               } else if (!this.isInsideCached(x, z - 1, baseBiome)) {
                  draw = true;
               }

               if (draw) {
                  editSession.getWorld().setBiome(position, material);
                  affected++;
               }
            }
         }
      }

      return affected;
   }
}
