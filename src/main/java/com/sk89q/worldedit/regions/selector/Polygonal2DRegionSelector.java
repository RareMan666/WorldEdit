package com.sk89q.worldedit.regions.selector;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.internal.cui.CUIRegion;
import com.sk89q.worldedit.internal.cui.SelectionMinMaxEvent;
import com.sk89q.worldedit.internal.cui.SelectionPoint2DEvent;
import com.sk89q.worldedit.internal.cui.SelectionShapeEvent;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.world.World;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public class Polygonal2DRegionSelector extends com.sk89q.worldedit.regions.Polygonal2DRegionSelector implements RegionSelector, CUIRegion {
   private transient BlockVector pos1;
   private transient Polygonal2DRegion region;

   public Polygonal2DRegionSelector() {
      this((World)null);
   }

   public Polygonal2DRegionSelector(@Nullable World world) {
      this.region = new Polygonal2DRegion(world);
   }

   public Polygonal2DRegionSelector(RegionSelector oldSelector) {
      this(((RegionSelector)Preconditions.checkNotNull(oldSelector)).getIncompleteRegion().getWorld());
      if (oldSelector instanceof Polygonal2DRegionSelector) {
         Polygonal2DRegionSelector polygonal2DRegionSelector = (Polygonal2DRegionSelector)oldSelector;
         this.pos1 = polygonal2DRegionSelector.pos1;
         this.region = new Polygonal2DRegion(polygonal2DRegionSelector.region);
      } else {
         Region oldRegion;
         try {
            oldRegion = oldSelector.getRegion();
         } catch (IncompleteRegionException var6) {
            return;
         }

         int minY = oldRegion.getMinimumPoint().getBlockY();
         int maxY = oldRegion.getMaximumPoint().getBlockY();
         List<BlockVector2D> points = oldRegion.polygonize(Integer.MAX_VALUE);
         this.pos1 = points.get(0).toVector(minY).toBlockVector();
         this.region = new Polygonal2DRegion(oldRegion.getWorld(), points, minY, maxY);
      }
   }

   @Deprecated
   public Polygonal2DRegionSelector(@Nullable LocalWorld world, List<BlockVector2D> points, int minY, int maxY) {
      this((World)world, points, minY, maxY);
   }

   public Polygonal2DRegionSelector(@Nullable World world, List<BlockVector2D> points, int minY, int maxY) {
      Preconditions.checkNotNull(points);
      BlockVector2D pos2D = points.get(0);
      this.pos1 = new BlockVector(pos2D.getX(), (double)minY, pos2D.getZ());
      this.region = new Polygonal2DRegion(world, points, minY, maxY);
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
      if (position.equals(this.pos1)) {
         return false;
      } else {
         this.pos1 = position.toBlockVector();
         this.region = new Polygonal2DRegion(this.region.getWorld());
         this.region.addPoint(position);
         this.region.expandY(position.getBlockY());
         return true;
      }
   }

   @Override
   public boolean selectSecondary(Vector position, SelectorLimits limits) {
      if (this.region.size() > 0) {
         List<BlockVector2D> points = this.region.getPoints();
         BlockVector2D lastPoint = points.get(this.region.size() - 1);
         if (lastPoint.getBlockX() == position.getBlockX() && lastPoint.getBlockZ() == position.getBlockZ()) {
            return false;
         }

         Optional<Integer> vertexLimit = limits.getPolygonVertexLimit();
         if (vertexLimit.isPresent() && points.size() > (Integer)vertexLimit.get()) {
            return false;
         }
      }

      this.region.addPoint(position);
      this.region.expandY(position.getBlockY());
      return true;
   }

   @Override
   public void explainPrimarySelection(Actor player, LocalSession session, Vector pos) {
      player.print("Starting a new polygon at " + pos + ".");
      session.dispatchCUIEvent(player, new SelectionShapeEvent(this.getTypeID()));
      session.dispatchCUIEvent(player, new SelectionPoint2DEvent(0, pos, this.getArea()));
      session.dispatchCUIEvent(player, new SelectionMinMaxEvent(this.region.getMinimumY(), this.region.getMaximumY()));
   }

   @Override
   public void explainSecondarySelection(Actor player, LocalSession session, Vector pos) {
      player.print("Added point #" + this.region.size() + " at " + pos + ".");
      session.dispatchCUIEvent(player, new SelectionPoint2DEvent(this.region.size() - 1, pos, this.getArea()));
      session.dispatchCUIEvent(player, new SelectionMinMaxEvent(this.region.getMinimumY(), this.region.getMaximumY()));
   }

   @Override
   public void explainRegionAdjust(Actor player, LocalSession session) {
      session.dispatchCUIEvent(player, new SelectionShapeEvent(this.getTypeID()));
      this.describeCUI(session, player);
   }

   @Override
   public BlockVector getPrimaryPosition() throws IncompleteRegionException {
      if (this.pos1 == null) {
         throw new IncompleteRegionException();
      } else {
         return this.pos1;
      }
   }

   public Polygonal2DRegion getRegion() throws IncompleteRegionException {
      if (!this.isDefined()) {
         throw new IncompleteRegionException();
      } else {
         return this.region;
      }
   }

   public Polygonal2DRegion getIncompleteRegion() {
      return this.region;
   }

   @Override
   public boolean isDefined() {
      return this.region.size() > 2;
   }

   @Override
   public void learnChanges() {
      BlockVector2D pt = this.region.getPoints().get(0);
      this.pos1 = new BlockVector(pt.getBlockX(), this.region.getMinimumPoint().getBlockY(), pt.getBlockZ());
   }

   @Override
   public void clear() {
      this.pos1 = null;
      this.region = new Polygonal2DRegion(this.region.getWorld());
   }

   @Override
   public String getTypeName() {
      return "2Dx1D polygon";
   }

   @Override
   public List<String> getInformationLines() {
      return Collections.singletonList("# points: " + this.region.size());
   }

   @Override
   public int getArea() {
      return this.region.getArea();
   }

   @Override
   public int getPointCount() {
      return this.region.getPoints().size();
   }

   @Override
   public void describeCUI(LocalSession session, Actor player) {
      List<BlockVector2D> points = this.region.getPoints();

      for (int id = 0; id < points.size(); id++) {
         session.dispatchCUIEvent(player, new SelectionPoint2DEvent(id, points.get(id), this.getArea()));
      }

      session.dispatchCUIEvent(player, new SelectionMinMaxEvent(this.region.getMinimumY(), this.region.getMaximumY()));
   }

   @Override
   public void describeLegacyCUI(LocalSession session, Actor player) {
      this.describeCUI(session, player);
   }

   @Override
   public int getProtocolVersion() {
      return 0;
   }

   @Override
   public String getTypeID() {
      return "polygon2d";
   }

   @Override
   public String getLegacyTypeID() {
      return "polygon2d";
   }
}
