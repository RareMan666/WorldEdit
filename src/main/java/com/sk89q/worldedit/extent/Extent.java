package com.sk89q.worldedit.extent;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import java.util.List;
import javax.annotation.Nullable;

public interface Extent extends InputExtent, OutputExtent {
   Vector getMinimumPoint();

   Vector getMaximumPoint();

   List<? extends Entity> getEntities(Region var1);

   List<? extends Entity> getEntities();

   @Nullable
   Entity createEntity(Location var1, BaseEntity var2);
}
