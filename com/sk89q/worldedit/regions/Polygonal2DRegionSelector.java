package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.internal.cui.CUIRegion;

@Deprecated
public abstract class Polygonal2DRegionSelector extends AbstractLegacyRegionSelector implements CUIRegion {
   public abstract int getPointCount();
}
