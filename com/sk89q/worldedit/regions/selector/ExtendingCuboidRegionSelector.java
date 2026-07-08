package com.sk89q.worldedit.regions.selector;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.world.World;
import javax.annotation.Nullable;

public class ExtendingCuboidRegionSelector extends CuboidRegionSelector {
   public ExtendingCuboidRegionSelector() {
      super((World)null);
   }

   public ExtendingCuboidRegionSelector(@Nullable World world) {
      super(world);
   }

   public ExtendingCuboidRegionSelector(RegionSelector oldSelector) {
      super(oldSelector);
      if (this.position1 != null && this.position2 != null) {
         this.position1 = this.region.getMinimumPoint().toBlockVector();
         this.position2 = this.region.getMaximumPoint().toBlockVector();
         this.region.setPos1(this.position1);
         this.region.setPos2(this.position2);
      }
   }

   public ExtendingCuboidRegionSelector(@Nullable World world, Vector position1, Vector position2) {
      this(world);
      position1 = Vector.getMinimum(position1, position2);
      position2 = Vector.getMaximum(position1, position2);
      this.region.setPos1(position1);
      this.region.setPos2(position2);
   }

   @Override
   public boolean selectPrimary(Vector position, SelectorLimits limits) {
      if (this.position1 != null
         && this.position2 != null
         && position.compareTo((Vector)this.position1) == 0
         && position.compareTo((Vector)this.position2) == 0) {
         return false;
      } else {
         this.position1 = this.position2 = position.toBlockVector();
         this.region.setPos1(this.position1);
         this.region.setPos2(this.position2);
         return true;
      }
   }

   @Override
   public boolean selectSecondary(Vector position, SelectorLimits limits) {
      if (this.position1 == null || this.position2 == null) {
         return this.selectPrimary(position, limits);
      } else if (this.region.contains(position)) {
         return false;
      } else {
         double x1 = Math.min(position.getX(), this.position1.getX());
         double y1 = Math.min(position.getY(), this.position1.getY());
         double z1 = Math.min(position.getZ(), this.position1.getZ());
         double x2 = Math.max(position.getX(), this.position2.getX());
         double y2 = Math.max(position.getY(), this.position2.getY());
         double z2 = Math.max(position.getZ(), this.position2.getZ());
         BlockVector o1 = this.position1;
         BlockVector o2 = this.position2;
         this.position1 = new BlockVector(x1, y1, z1);
         this.position2 = new BlockVector(x2, y2, z2);
         this.region.setPos1(this.position1);
         this.region.setPos2(this.position2);

         assert this.region.contains(o1);

         assert this.region.contains(o2);

         assert this.region.contains(position);

         return true;
      }
   }

   @Override
   public void explainPrimarySelection(Actor player, LocalSession session, Vector pos) {
      player.print("Started selection at " + pos + " (" + this.region.getArea() + ").");
      this.explainRegionAdjust(player, session);
   }

   @Override
   public void explainSecondarySelection(Actor player, LocalSession session, Vector pos) {
      player.print("Extended selection to encompass " + pos + " (" + this.region.getArea() + ").");
      this.explainRegionAdjust(player, session);
   }
}
