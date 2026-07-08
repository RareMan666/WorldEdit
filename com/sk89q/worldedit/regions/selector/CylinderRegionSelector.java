package com.sk89q.worldedit.regions.selector;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.internal.cui.CUIRegion;
import com.sk89q.worldedit.internal.cui.SelectionCylinderEvent;
import com.sk89q.worldedit.internal.cui.SelectionMinMaxEvent;
import com.sk89q.worldedit.internal.cui.SelectionPointEvent;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.world.World;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;

public class CylinderRegionSelector extends com.sk89q.worldedit.regions.CylinderRegionSelector implements RegionSelector, CUIRegion {
   protected static final transient NumberFormat NUMBER_FORMAT = (NumberFormat)NumberFormat.getInstance().clone();
   protected transient CylinderRegion region;

   public CylinderRegionSelector() {
      this((World)null);
   }

   public CylinderRegionSelector(@Nullable World world) {
      this.region = new CylinderRegion(world);
   }

   public CylinderRegionSelector(RegionSelector oldSelector) {
      this(((RegionSelector)Preconditions.checkNotNull(oldSelector)).getIncompleteRegion().getWorld());
      if (oldSelector instanceof CylinderRegionSelector) {
         CylinderRegionSelector cylSelector = (CylinderRegionSelector)oldSelector;
         this.region = new CylinderRegion(cylSelector.region);
      } else {
         Region oldRegion;
         try {
            oldRegion = oldSelector.getRegion();
         } catch (IncompleteRegionException var6) {
            return;
         }

         Vector pos1 = oldRegion.getMinimumPoint();
         Vector pos2 = oldRegion.getMaximumPoint();
         Vector center = pos1.add(pos2).divide(2).floor();
         this.region.setCenter(center.toVector2D());
         this.region.setRadius(pos2.toVector2D().subtract(center.toVector2D()));
         this.region.setMaximumY(Math.max(pos1.getBlockY(), pos2.getBlockY()));
         this.region.setMinimumY(Math.min(pos1.getBlockY(), pos2.getBlockY()));
      }
   }

   public CylinderRegionSelector(@Nullable World world, Vector2D center, Vector2D radius, int minY, int maxY) {
      this(world);
      this.region.setCenter(center);
      this.region.setRadius(radius);
      this.region.setMinimumY(Math.min(minY, maxY));
      this.region.setMaximumY(Math.max(minY, maxY));
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
      if (!this.region.getCenter().equals(Vector.ZERO) && position.compareTo(this.region.getCenter()) == 0) {
         return false;
      } else {
         this.region = new CylinderRegion(this.region.getWorld());
         this.region.setCenter(position.toVector2D());
         this.region.setY(position.getBlockY());
         return true;
      }
   }

   @Override
   public boolean selectSecondary(Vector position, SelectorLimits limits) {
      Vector center = this.region.getCenter();
      if (center.compareTo(Vector.ZERO) == 0) {
         return true;
      } else {
         Vector2D diff = position.subtract(center).toVector2D();
         Vector2D minRadius = Vector2D.getMaximum(diff, diff.multiply(-1.0));
         this.region.extendRadius(minRadius);
         this.region.setY(position.getBlockY());
         return true;
      }
   }

   @Override
   public void explainPrimarySelection(Actor player, LocalSession session, Vector pos) {
      player.print("Starting a new cylindrical selection at " + pos + ".");
      session.describeCUI(player);
   }

   @Override
   public void explainSecondarySelection(Actor player, LocalSession session, Vector pos) {
      Vector center = this.region.getCenter();
      if (!center.equals(Vector.ZERO)) {
         player.print(
            "Radius set to "
               + NUMBER_FORMAT.format(this.region.getRadius().getX())
               + "/"
               + NUMBER_FORMAT.format(this.region.getRadius().getZ())
               + " blocks. ("
               + this.region.getArea()
               + ")."
         );
         session.describeCUI(player);
      } else {
         player.printError("You must select the center point before setting the radius.");
      }
   }

   @Override
   public void explainRegionAdjust(Actor player, LocalSession session) {
      session.describeCUI(player);
   }

   @Override
   public BlockVector getPrimaryPosition() throws IncompleteRegionException {
      if (!this.isDefined()) {
         throw new IncompleteRegionException();
      } else {
         return this.region.getCenter().toBlockVector();
      }
   }

   public CylinderRegion getRegion() throws IncompleteRegionException {
      if (!this.isDefined()) {
         throw new IncompleteRegionException();
      } else {
         return this.region;
      }
   }

   public CylinderRegion getIncompleteRegion() {
      return this.region;
   }

   @Override
   public boolean isDefined() {
      return !this.region.getRadius().equals(Vector2D.ZERO);
   }

   @Override
   public void learnChanges() {
   }

   @Override
   public void clear() {
      this.region = new CylinderRegion(this.region.getWorld());
   }

   @Override
   public String getTypeName() {
      return "Cylinder";
   }

   @Override
   public List<String> getInformationLines() {
      List<String> lines = new ArrayList<>();
      if (!this.region.getCenter().equals(Vector.ZERO)) {
         lines.add("Center: " + this.region.getCenter());
      }

      if (!this.region.getRadius().equals(Vector2D.ZERO)) {
         lines.add("Radius: " + this.region.getRadius());
      }

      return lines;
   }

   @Override
   public int getArea() {
      return this.region.getArea();
   }

   @Override
   public void describeCUI(LocalSession session, Actor player) {
      session.dispatchCUIEvent(player, new SelectionCylinderEvent(this.region.getCenter(), this.region.getRadius()));
      session.dispatchCUIEvent(player, new SelectionMinMaxEvent(this.region.getMinimumY(), this.region.getMaximumY()));
   }

   @Override
   public void describeLegacyCUI(LocalSession session, Actor player) {
      if (this.isDefined()) {
         session.dispatchCUIEvent(player, new SelectionPointEvent(0, this.region.getMinimumPoint(), this.getArea()));
         session.dispatchCUIEvent(player, new SelectionPointEvent(1, this.region.getMaximumPoint(), this.getArea()));
      }
   }

   @Override
   public int getProtocolVersion() {
      return 1;
   }

   @Override
   public String getTypeID() {
      return "cylinder";
   }

   @Override
   public String getLegacyTypeID() {
      return "cuboid";
   }

   static {
      NUMBER_FORMAT.setMaximumFractionDigits(3);
   }
}
