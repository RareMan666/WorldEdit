package com.sk89q.worldedit.regions.selector;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.internal.cui.CUIRegion;
import com.sk89q.worldedit.internal.cui.SelectionEllipsoidPointEvent;
import com.sk89q.worldedit.internal.cui.SelectionPointEvent;
import com.sk89q.worldedit.regions.EllipsoidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.world.World;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class EllipsoidRegionSelector extends com.sk89q.worldedit.regions.EllipsoidRegionSelector implements RegionSelector, CUIRegion {
   protected transient EllipsoidRegion region;
   protected transient boolean started = false;

   public EllipsoidRegionSelector() {
      this((World)null);
   }

   public EllipsoidRegionSelector(@Nullable World world) {
      this.region = new EllipsoidRegion(world, new Vector(), new Vector());
   }

   public EllipsoidRegionSelector(RegionSelector oldSelector) {
      this(((RegionSelector)Preconditions.checkNotNull(oldSelector)).getIncompleteRegion().getWorld());
      if (oldSelector instanceof EllipsoidRegionSelector) {
         EllipsoidRegionSelector ellipsoidRegionSelector = (EllipsoidRegionSelector)oldSelector;
         this.region = new EllipsoidRegion(ellipsoidRegionSelector.getIncompleteRegion());
      } else {
         Region oldRegion;
         try {
            oldRegion = oldSelector.getRegion();
         } catch (IncompleteRegionException var6) {
            return;
         }

         BlockVector pos1 = oldRegion.getMinimumPoint().toBlockVector();
         BlockVector pos2 = oldRegion.getMaximumPoint().toBlockVector();
         Vector center = pos1.add(pos2).divide(2).floor();
         this.region.setCenter(center);
         this.region.setRadius(pos2.subtract(center));
      }
   }

   public EllipsoidRegionSelector(@Nullable World world, Vector center, Vector radius) {
      this(world);
      this.region.setCenter(center);
      this.region.setRadius(radius);
   }

   @Nullable
   @Override
   public World getWorld() {
      return this.region.getWorld();
   }

   @Override
   public void setWorld(@Nullable World world) {
      this.region.setWorld(world);
   }

   @Override
   public boolean selectPrimary(Vector position, SelectorLimits limits) {
      if (position.equals(this.region.getCenter()) && this.region.getRadius().lengthSq() == 0.0) {
         return false;
      } else {
         this.region.setCenter(position.toBlockVector());
         this.region.setRadius(new Vector());
         this.started = true;
         return true;
      }
   }

   @Override
   public boolean selectSecondary(Vector position, SelectorLimits limits) {
      if (!this.started) {
         return false;
      } else {
         Vector diff = position.subtract(this.region.getCenter());
         Vector minRadius = Vector.getMaximum(diff, diff.multiply(-1.0));
         this.region.extendRadius(minRadius);
         return true;
      }
   }

   @Override
   public void explainPrimarySelection(Actor player, LocalSession session, Vector pos) {
      if (this.isDefined()) {
         player.print("Center position set to " + this.region.getCenter() + " (" + this.region.getArea() + ").");
      } else {
         player.print("Center position set to " + this.region.getCenter() + ".");
      }

      session.describeCUI(player);
   }

   @Override
   public void explainSecondarySelection(Actor player, LocalSession session, Vector pos) {
      if (this.isDefined()) {
         player.print("Radius set to " + this.region.getRadius() + " (" + this.region.getArea() + ").");
      } else {
         player.print("Radius set to " + this.region.getRadius() + ".");
      }

      session.describeCUI(player);
   }

   @Override
   public void explainRegionAdjust(Actor player, LocalSession session) {
      session.describeCUI(player);
   }

   @Override
   public boolean isDefined() {
      return this.started && this.region.getRadius().lengthSq() > 0.0;
   }

   public EllipsoidRegion getRegion() throws IncompleteRegionException {
      if (!this.isDefined()) {
         throw new IncompleteRegionException();
      } else {
         return this.region;
      }
   }

   public EllipsoidRegion getIncompleteRegion() {
      return this.region;
   }

   @Override
   public void learnChanges() {
   }

   @Override
   public void clear() {
      this.region.setCenter(new Vector());
      this.region.setRadius(new Vector());
   }

   @Override
   public String getTypeName() {
      return "ellipsoid";
   }

   @Override
   public List<String> getInformationLines() {
      List<String> lines = new ArrayList<>();
      Vector center = this.region.getCenter();
      if (center.lengthSq() > 0.0) {
         lines.add("Center: " + center);
      }

      Vector radius = this.region.getRadius();
      if (radius.lengthSq() > 0.0) {
         lines.add("X/Y/Z radius: " + radius);
      }

      return lines;
   }

   @Override
   public int getArea() {
      return this.region.getArea();
   }

   @Override
   public void describeCUI(LocalSession session, Actor player) {
      session.dispatchCUIEvent(player, new SelectionEllipsoidPointEvent(0, this.region.getCenter()));
      session.dispatchCUIEvent(player, new SelectionEllipsoidPointEvent(1, this.region.getRadius()));
   }

   @Override
   public void describeLegacyCUI(LocalSession session, Actor player) {
      session.dispatchCUIEvent(player, new SelectionPointEvent(0, this.region.getMinimumPoint(), this.getArea()));
      session.dispatchCUIEvent(player, new SelectionPointEvent(1, this.region.getMaximumPoint(), this.getArea()));
   }

   @Override
   public String getLegacyTypeID() {
      return "cuboid";
   }

   @Override
   public int getProtocolVersion() {
      return 1;
   }

   @Override
   public String getTypeID() {
      return "ellipsoid";
   }

   @Override
   public BlockVector getPrimaryPosition() throws IncompleteRegionException {
      return this.region.getCenter().toBlockVector();
   }
}
