package com.sk89q.worldedit.regions.selector;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.internal.cui.CUIRegion;
import com.sk89q.worldedit.internal.cui.SelectionPointEvent;
import com.sk89q.worldedit.internal.cui.SelectionPolygonEvent;
import com.sk89q.worldedit.regions.ConvexPolyhedralRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.polyhedron.Triangle;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;
import com.sk89q.worldedit.world.World;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class ConvexPolyhedralRegionSelector extends com.sk89q.worldedit.regions.ConvexPolyhedralRegionSelector implements RegionSelector, CUIRegion {
   private final transient ConvexPolyhedralRegion region;
   private transient BlockVector pos1;

   public ConvexPolyhedralRegionSelector() {
      this((World)null);
   }

   public ConvexPolyhedralRegionSelector(@Nullable World world) {
      this.region = new ConvexPolyhedralRegion(world);
   }

   public ConvexPolyhedralRegionSelector(RegionSelector oldSelector) {
      Preconditions.checkNotNull(oldSelector);
      if (oldSelector instanceof ConvexPolyhedralRegionSelector) {
         ConvexPolyhedralRegionSelector convexPolyhedralRegionSelector = (ConvexPolyhedralRegionSelector)oldSelector;
         this.pos1 = convexPolyhedralRegionSelector.pos1;
         this.region = new ConvexPolyhedralRegion(convexPolyhedralRegionSelector.region);
      } else {
         Region oldRegion;
         try {
            oldRegion = oldSelector.getRegion();
         } catch (IncompleteRegionException var7) {
            this.region = new ConvexPolyhedralRegion(oldSelector.getIncompleteRegion().getWorld());
            return;
         }

         int minY = oldRegion.getMinimumPoint().getBlockY();
         int maxY = oldRegion.getMaximumPoint().getBlockY();
         this.region = new ConvexPolyhedralRegion(oldRegion.getWorld());

         for (BlockVector2D pt : new ArrayList<>(oldRegion.polygonize(Integer.MAX_VALUE))) {
            this.region.addVertex(pt.toVector(minY));
            this.region.addVertex(pt.toVector(maxY));
         }

         this.learnChanges();
      }
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
      Preconditions.checkNotNull(position);
      this.clear();
      this.pos1 = position.toBlockVector();
      return this.region.addVertex(position);
   }

   @Override
   public boolean selectSecondary(Vector position, SelectorLimits limits) {
      Preconditions.checkNotNull(position);
      Optional<Integer> vertexLimit = limits.getPolyhedronVertexLimit();
      return vertexLimit.isPresent() && this.region.getVertices().size() > vertexLimit.get() ? false : this.region.addVertex(position);
   }

   @Override
   public BlockVector getPrimaryPosition() throws IncompleteRegionException {
      return this.pos1;
   }

   @Override
   public Region getRegion() throws IncompleteRegionException {
      if (!this.region.isDefined()) {
         throw new IncompleteRegionException();
      } else {
         return this.region;
      }
   }

   @Override
   public Region getIncompleteRegion() {
      return this.region;
   }

   @Override
   public boolean isDefined() {
      return this.region.isDefined();
   }

   @Override
   public int getArea() {
      return this.region.getArea();
   }

   @Override
   public void learnChanges() {
      this.pos1 = this.region.getVertices().iterator().next().toBlockVector();
   }

   @Override
   public void clear() {
      this.region.clear();
   }

   @Override
   public String getTypeName() {
      return "Convex Polyhedron";
   }

   @Override
   public List<String> getInformationLines() {
      List<String> ret = new ArrayList<>();
      ret.add("Vertices: " + this.region.getVertices().size());
      ret.add("Triangles: " + this.region.getTriangles().size());
      return ret;
   }

   @Override
   public void explainPrimarySelection(Actor player, LocalSession session, Vector pos) {
      Preconditions.checkNotNull(player);
      Preconditions.checkNotNull(session);
      Preconditions.checkNotNull(pos);
      session.describeCUI(player);
      player.print("Started new selection with vertex " + pos + ".");
   }

   @Override
   public void explainSecondarySelection(Actor player, LocalSession session, Vector pos) {
      Preconditions.checkNotNull(player);
      Preconditions.checkNotNull(session);
      Preconditions.checkNotNull(pos);
      session.describeCUI(player);
      player.print("Added vertex " + pos + " to the selection.");
   }

   @Override
   public void explainRegionAdjust(Actor player, LocalSession session) {
      Preconditions.checkNotNull(player);
      Preconditions.checkNotNull(session);
      session.describeCUI(player);
   }

   @Override
   public int getProtocolVersion() {
      return 3;
   }

   @Override
   public String getTypeID() {
      return "polyhedron";
   }

   @Override
   public void describeCUI(LocalSession session, Actor player) {
      Preconditions.checkNotNull(player);
      Preconditions.checkNotNull(session);
      Collection<Vector> vertices = this.region.getVertices();
      Collection<Triangle> triangles = this.region.getTriangles();
      Map<Vector, Integer> vertexIds = new HashMap<>(vertices.size());
      int lastVertexId = -1;

      for (Vector vertex : vertices) {
         vertexIds.put(vertex, ++lastVertexId);
         session.dispatchCUIEvent(player, new SelectionPointEvent(lastVertexId, vertex, this.getArea()));
      }

      for (Triangle triangle : triangles) {
         int[] v = new int[3];

         for (int i = 0; i < 3; i++) {
            v[i] = vertexIds.get(triangle.getVertex(i));
         }

         session.dispatchCUIEvent(player, new SelectionPolygonEvent(v));
      }
   }

   @Override
   public String getLegacyTypeID() {
      return "cuboid";
   }

   @Override
   public void describeLegacyCUI(LocalSession session, Actor player) {
      Preconditions.checkNotNull(player);
      Preconditions.checkNotNull(session);
      if (this.isDefined()) {
         session.dispatchCUIEvent(player, new SelectionPointEvent(0, this.region.getMinimumPoint(), this.getArea()));
         session.dispatchCUIEvent(player, new SelectionPointEvent(1, this.region.getMaximumPoint(), this.getArea()));
      }
   }
}
