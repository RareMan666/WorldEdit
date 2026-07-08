package com.sk89q.worldedit.entity;

import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.util.Faceted;
import com.sk89q.worldedit.util.Location;
import javax.annotation.Nullable;

public interface Entity extends Faceted {
   @Nullable
   BaseEntity getState();

   Location getLocation();

   Extent getExtent();

   boolean remove();
}
