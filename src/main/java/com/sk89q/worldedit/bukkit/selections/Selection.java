package com.sk89q.worldedit.bukkit.selections;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.RegionSelector;
import javax.annotation.Nullable;
import org.bukkit.Location;
import org.bukkit.World;

public interface Selection {
   Location getMinimumPoint();

   Vector getNativeMinimumPoint();

   Location getMaximumPoint();

   Vector getNativeMaximumPoint();

   RegionSelector getRegionSelector();

   @Nullable
   World getWorld();

   int getArea();

   int getWidth();

   int getHeight();

   int getLength();

   boolean contains(Location var1);
}
