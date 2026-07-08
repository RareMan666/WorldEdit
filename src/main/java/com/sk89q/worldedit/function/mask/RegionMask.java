package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.Region;
import javax.annotation.Nullable;

public class RegionMask extends AbstractMask {
   private Region region;

   public RegionMask(Region region) {
      this.setRegion(region);
   }

   public Region getRegion() {
      return this.region;
   }

   public void setRegion(Region region) {
      Preconditions.checkNotNull(region);
      this.region = region;
   }

   @Override
   public boolean test(Vector vector) {
      return this.region.contains(vector);
   }

   @Nullable
   @Override
   public Mask2D toMask2D() {
      return null;
   }
}
