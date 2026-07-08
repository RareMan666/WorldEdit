package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.Vector2D;

public interface FlatRegion extends Region {
   int getMinimumY();

   int getMaximumY();

   Iterable<Vector2D> asFlatRegion();
}
