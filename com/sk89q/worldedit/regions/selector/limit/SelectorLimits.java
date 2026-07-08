package com.sk89q.worldedit.regions.selector.limit;

import com.google.common.base.Optional;

public interface SelectorLimits {
   Optional<Integer> getPolygonVertexLimit();

   Optional<Integer> getPolyhedronVertexLimit();
}
