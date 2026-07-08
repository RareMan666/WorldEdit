package com.sk89q.worldedit.regions.factory;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;

public class CuboidRegionFactory implements RegionFactory {
   @Override
   public Region createCenteredAt(Vector position, double size) {
      return CuboidRegion.fromCenter(position, (int)size);
   }
}
