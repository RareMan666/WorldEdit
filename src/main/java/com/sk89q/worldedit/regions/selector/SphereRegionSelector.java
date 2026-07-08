package com.sk89q.worldedit.regions.selector;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.world.World;
import javax.annotation.Nullable;

public class SphereRegionSelector extends EllipsoidRegionSelector {
   public SphereRegionSelector() {
   }

   public SphereRegionSelector(@Nullable World world) {
      super(world);
   }

   public SphereRegionSelector(RegionSelector oldSelector) {
      super(oldSelector);
      Vector radius = this.region.getRadius();
      double radiusScalar = Math.max(Math.max(radius.getX(), radius.getY()), radius.getZ());
      this.region.setRadius(new Vector(radiusScalar, radiusScalar, radiusScalar));
   }

   public SphereRegionSelector(@Nullable World world, Vector center, int radius) {
      super(world, center, new Vector(radius, radius, radius));
   }

   @Override
   public boolean selectSecondary(Vector position, SelectorLimits limits) {
      if (!this.started) {
         return false;
      } else {
         double radiusScalar = Math.ceil(position.distance(this.region.getCenter()));
         this.region.setRadius(new Vector(radiusScalar, radiusScalar, radiusScalar));
         return true;
      }
   }

   @Override
   public void explainSecondarySelection(Actor player, LocalSession session, Vector pos) {
      if (this.isDefined()) {
         player.print("Radius set to " + this.region.getRadius().getX() + " (" + this.region.getArea() + ").");
      } else {
         player.print("Radius set to " + this.region.getRadius().getX() + ".");
      }

      session.describeCUI(player);
   }

   @Override
   public String getTypeName() {
      return "sphere";
   }
}
