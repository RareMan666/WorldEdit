package com.sk89q.worldedit.regions.selector;

import com.sk89q.worldedit.regions.RegionSelector;

public enum RegionSelectorType {
   CUBOID(CuboidRegionSelector.class),
   EXTENDING_CUBOID(ExtendingCuboidRegionSelector.class),
   CYLINDER(CylinderRegionSelector.class),
   SPHERE(SphereRegionSelector.class),
   ELLIPSOID(EllipsoidRegionSelector.class),
   POLYGON(Polygonal2DRegionSelector.class),
   CONVEX_POLYHEDRON(ConvexPolyhedralRegionSelector.class);

   private final Class<? extends RegionSelector> selectorClass;

   private RegionSelectorType(Class<? extends RegionSelector> selectorClass) {
      this.selectorClass = selectorClass;
   }

   public Class<? extends RegionSelector> getSelectorClass() {
      return this.selectorClass;
   }

   public RegionSelector createSelector() {
      try {
         return this.getSelectorClass().newInstance();
      } catch (InstantiationException var2) {
         throw new RuntimeException("Could not create selector", var2);
      } catch (IllegalAccessException var3) {
         throw new RuntimeException("Could not create selector", var3);
      }
   }
}
