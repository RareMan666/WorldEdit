package com.sk89q.worldedit.bukkit.selections;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.CylinderRegionSelector;
import org.bukkit.World;

public class CylinderSelection extends RegionSelection {
   private CylinderRegion cylRegion;

   public CylinderSelection(World world, RegionSelector selector, CylinderRegion region) {
      super(world, selector, region);
      this.cylRegion = region;
   }

   public CylinderSelection(World world, BlockVector2D center, BlockVector2D radius, int minY, int maxY) {
      super(world);
      LocalWorld lWorld = BukkitUtil.getLocalWorld(world);
      minY = Math.min(Math.max(0, minY), world.getMaxHeight());
      maxY = Math.min(Math.max(0, maxY), world.getMaxHeight());
      CylinderRegionSelector sel = new CylinderRegionSelector(lWorld, center, radius, minY, maxY);
      this.cylRegion = sel.getIncompleteRegion();
      this.setRegionSelector(sel);
      this.setRegion(this.cylRegion);
   }

   public BlockVector2D getCenter() {
      return this.cylRegion.getCenter().toVector2D().toBlockVector2D();
   }

   public BlockVector2D getRadius() {
      return this.cylRegion.getRadius().toBlockVector2D();
   }
}
