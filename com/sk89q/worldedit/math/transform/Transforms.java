package com.sk89q.worldedit.math.transform;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.util.Location;

public final class Transforms {
   private Transforms() {
   }

   public static Location transform(Location location, Transform transform) {
      Preconditions.checkNotNull(location);
      Preconditions.checkNotNull(transform);
      return new Location(location.getExtent(), transform.apply(location.toVector()), location.getDirection());
   }
}
