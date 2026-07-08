package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;

@Deprecated
public class RegionMask extends AbstractMask {
   private final Region region;

   public RegionMask(Region region) {
      this.region = region.clone();
   }

   @Override
   public boolean matches(EditSession editSession, Vector position) {
      return this.region.contains(position);
   }
}
