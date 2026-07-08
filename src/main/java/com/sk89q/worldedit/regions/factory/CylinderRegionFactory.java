package com.sk89q.worldedit.regions.factory;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.Region;

public class CylinderRegionFactory implements RegionFactory {
   private final double height;

   public CylinderRegionFactory(double height) {
      this.height = height;
   }

   @Override
   public Region createCenteredAt(Vector position, double size) {
      return new CylinderRegion(
         position, new Vector2D(size, size), position.getBlockY() - (int)(this.height / 2.0), position.getBlockY() + (int)(this.height / 2.0)
      );
   }
}
