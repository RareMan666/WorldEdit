package com.sk89q.worldedit.math.convolution;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.regions.Region;

public class HeightMap {
   private int[] data;
   private int width;
   private int height;
   private Region region;
   private EditSession session;

   public HeightMap(EditSession session, Region region) {
      this(session, region, false);
   }

   public HeightMap(EditSession session, Region region, boolean naturalOnly) {
      Preconditions.checkNotNull(session);
      Preconditions.checkNotNull(region);
      this.session = session;
      this.region = region;
      this.width = region.getWidth();
      this.height = region.getLength();
      int minX = region.getMinimumPoint().getBlockX();
      int minY = region.getMinimumPoint().getBlockY();
      int minZ = region.getMinimumPoint().getBlockZ();
      int maxY = region.getMaximumPoint().getBlockY();
      this.data = new int[this.width * this.height];

      for (int z = 0; z < this.height; z++) {
         for (int x = 0; x < this.width; x++) {
            this.data[z * this.width + x] = session.getHighestTerrainBlock(x + minX, z + minZ, minY, maxY, naturalOnly);
         }
      }
   }

   public int applyFilter(HeightMapFilter filter, int iterations) throws MaxChangedBlocksException {
      Preconditions.checkNotNull(filter);
      int[] newData = new int[this.data.length];
      System.arraycopy(this.data, 0, newData, 0, this.data.length);

      for (int i = 0; i < iterations; i++) {
         newData = filter.filter(newData, this.width, this.height);
      }

      return this.apply(newData);
   }

   public int apply(int[] data) throws MaxChangedBlocksException {
      Preconditions.checkNotNull(data);
      Vector minY = this.region.getMinimumPoint();
      int originX = minY.getBlockX();
      int originY = minY.getBlockY();
      int originZ = minY.getBlockZ();
      int maxY = this.region.getMaximumPoint().getBlockY();
      BaseBlock fillerAir = new BaseBlock(0);
      int blocksChanged = 0;

      for (int z = 0; z < this.height; z++) {
         for (int x = 0; x < this.width; x++) {
            int index = z * this.width + x;
            int curHeight = this.data[index];
            int newHeight = Math.min(maxY, data[index]);
            int xr = x + originX;
            int zr = z + originZ;
            double scale = (double)(curHeight - originY) / (newHeight - originY);
            if (newHeight > curHeight) {
               BaseBlock existing = this.session.getBlock(new Vector(xr, curHeight, zr));
               if (existing.getType() != 8 && existing.getType() != 9 && existing.getType() != 10 && existing.getType() != 11) {
                  this.session.setBlock(new Vector(xr, newHeight, zr), existing);
                  blocksChanged++;

                  for (int y = newHeight - 1 - originY; y >= 0; y--) {
                     int copyFrom = (int)(y * scale);
                     this.session.setBlock(new Vector(xr, originY + y, zr), this.session.getBlock(new Vector(xr, originY + copyFrom, zr)));
                     blocksChanged++;
                  }
               }
            } else if (curHeight > newHeight) {
               for (int y = 0; y < newHeight - originY; y++) {
                  int copyFrom = (int)(y * scale);
                  this.session.setBlock(new Vector(xr, originY + y, zr), this.session.getBlock(new Vector(xr, originY + copyFrom, zr)));
                  blocksChanged++;
               }

               this.session.setBlock(new Vector(xr, newHeight, zr), this.session.getBlock(new Vector(xr, curHeight, zr)));
               blocksChanged++;

               for (int y = newHeight + 1; y <= curHeight; y++) {
                  this.session.setBlock(new Vector(xr, y, zr), fillerAir);
                  blocksChanged++;
               }
            }
         }
      }

      return blocksChanged;
   }
}
