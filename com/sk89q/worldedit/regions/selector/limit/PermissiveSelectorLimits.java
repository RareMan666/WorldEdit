package com.sk89q.worldedit.regions.selector.limit;

import com.google.common.base.Optional;

public class PermissiveSelectorLimits implements SelectorLimits {
   private static final PermissiveSelectorLimits INSTANCE = new PermissiveSelectorLimits();

   private PermissiveSelectorLimits() {
   }

   @Override
   public Optional<Integer> getPolygonVertexLimit() {
      return Optional.absent();
   }

   @Override
   public Optional<Integer> getPolyhedronVertexLimit() {
      return Optional.absent();
   }

   public static PermissiveSelectorLimits getInstance() {
      return INSTANCE;
   }
}
