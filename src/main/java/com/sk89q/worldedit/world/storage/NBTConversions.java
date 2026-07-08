package com.sk89q.worldedit.world.storage;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.ListTag;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.util.Location;

public final class NBTConversions {
   private NBTConversions() {
   }

   public static Location toLocation(Extent extent, ListTag positionTag, ListTag directionTag) {
      Preconditions.checkNotNull(extent);
      Preconditions.checkNotNull(positionTag);
      Preconditions.checkNotNull(directionTag);
      return new Location(
         extent, positionTag.asDouble(0), positionTag.asDouble(1), positionTag.asDouble(2), (float)directionTag.asDouble(0), (float)directionTag.asDouble(1)
      );
   }
}
