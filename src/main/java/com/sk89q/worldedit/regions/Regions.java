package com.sk89q.worldedit.regions;

public final class Regions {
   private Regions() {
   }

   public static double minimumY(Region region) {
      return region.getMinimumPoint().getY();
   }

   public static double maximumY(Region region) {
      return region.getMaximumPoint().getY();
   }

   public static int minimumBlockY(Region region) {
      return region.getMinimumPoint().getBlockY();
   }

   public static int maximumBlockY(Region region) {
      return region.getMaximumPoint().getBlockY();
   }

   public static FlatRegion asFlatRegion(Region region) {
      return (FlatRegion)(region instanceof FlatRegion ? (FlatRegion)region : CuboidRegion.makeCuboid(region));
   }
}
