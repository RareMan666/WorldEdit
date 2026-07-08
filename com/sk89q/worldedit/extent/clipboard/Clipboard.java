package com.sk89q.worldedit.extent.clipboard;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.regions.Region;

public interface Clipboard extends Extent {
   Region getRegion();

   Vector getDimensions();

   Vector getOrigin();

   void setOrigin(Vector var1);
}
