package com.sk89q.worldedit.regions.factory;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.EllipsoidRegion;
import com.sk89q.worldedit.regions.Region;

public class SphereRegionFactory implements RegionFactory {
   @Override
   public Region createCenteredAt(Vector position, double size) {
      return new EllipsoidRegion(position, new Vector(size, size, size));
   }
}
