package com.sk89q.worldedit.regions;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.geom.Polygons;
import com.sk89q.worldedit.regions.iterator.FlatRegion3DIterator;
import com.sk89q.worldedit.regions.iterator.FlatRegionIterator;
import com.sk89q.worldedit.world.World;
import java.util.Iterator;
import java.util.List;

public class CylinderRegion extends AbstractRegion implements FlatRegion {
   private Vector2D center;
   private Vector2D radius;
   private int minY;
   private int maxY;
   private boolean hasY = false;

   public CylinderRegion() {
      this((World)null);
   }

   @Deprecated
   public CylinderRegion(LocalWorld world) {
      this((World)world);
   }

   public CylinderRegion(World world) {
      this(world, new Vector(), new Vector2D(), 0, 0);
      this.hasY = false;
   }

   @Deprecated
   public CylinderRegion(LocalWorld world, Vector center, Vector2D radius, int minY, int maxY) {
      this((World)world, center, radius, minY, maxY);
   }

   public CylinderRegion(World world, Vector center, Vector2D radius, int minY, int maxY) {
      super(world);
      this.setCenter(center.toVector2D());
      this.setRadius(radius);
      this.minY = minY;
      this.maxY = maxY;
      this.hasY = true;
   }

   public CylinderRegion(Vector center, Vector2D radius, int minY, int maxY) {
      super(null);
      this.setCenter(center.toVector2D());
      this.setRadius(radius);
      this.minY = minY;
      this.maxY = maxY;
      this.hasY = true;
   }

   public CylinderRegion(CylinderRegion region) {
      this(region.world, region.getCenter(), region.getRadius(), region.minY, region.maxY);
      this.hasY = region.hasY;
   }

   @Override
   public Vector getCenter() {
      return this.center.toVector((this.maxY + this.minY) / 2);
   }

   @Deprecated
   public void setCenter(Vector center) {
      this.setCenter(center.toVector2D());
   }

   public void setCenter(Vector2D center) {
      this.center = center;
   }

   public Vector2D getRadius() {
      return this.radius.subtract(0.5, 0.5);
   }

   public void setRadius(Vector2D radius) {
      this.radius = radius.add(0.5, 0.5);
   }

   public void extendRadius(Vector2D minRadius) {
      this.setRadius(Vector2D.getMaximum(minRadius, this.getRadius()));
   }

   public void setMinimumY(int y) {
      this.hasY = true;
      this.minY = y;
   }

   public void setMaximumY(int y) {
      this.hasY = true;
      this.maxY = y;
   }

   @Override
   public Vector getMinimumPoint() {
      return this.center.subtract(this.getRadius()).toVector(this.minY);
   }

   @Override
   public Vector getMaximumPoint() {
      return this.center.add(this.getRadius()).toVector(this.maxY);
   }

   @Override
   public int getMaximumY() {
      return this.maxY;
   }

   @Override
   public int getMinimumY() {
      return this.minY;
   }

   @Override
   public int getArea() {
      return (int)Math.floor(this.radius.getX() * this.radius.getZ() * Math.PI * this.getHeight());
   }

   @Override
   public int getWidth() {
      return (int)(2.0 * this.radius.getX());
   }

   @Override
   public int getHeight() {
      return this.maxY - this.minY + 1;
   }

   @Override
   public int getLength() {
      return (int)(2.0 * this.radius.getZ());
   }

   private Vector2D calculateDiff2D(Vector... changes) throws RegionOperationException {
      Vector2D diff = new Vector2D();

      for (Vector change : changes) {
         diff = diff.add(change.toVector2D());
      }

      if ((diff.getBlockX() & 1) + (diff.getBlockZ() & 1) != 0) {
         throw new RegionOperationException("Cylinders changes must be even for each horizontal dimensions.");
      } else {
         return diff.divide(2).floor();
      }
   }

   private Vector2D calculateChanges2D(Vector... changes) {
      Vector2D total = new Vector2D();

      for (Vector change : changes) {
         total = total.add(change.toVector2D().positive());
      }

      return total.divide(2).floor();
   }

   @Override
   public void expand(Vector... changes) throws RegionOperationException {
      this.center = this.center.add(this.calculateDiff2D(changes));
      this.radius = this.radius.add(this.calculateChanges2D(changes));

      for (Vector change : changes) {
         int changeY = change.getBlockY();
         if (changeY > 0) {
            this.maxY += changeY;
         } else {
            this.minY += changeY;
         }
      }
   }

   @Override
   public void contract(Vector... changes) throws RegionOperationException {
      this.center = this.center.subtract(this.calculateDiff2D(changes));
      Vector2D newRadius = this.radius.subtract(this.calculateChanges2D(changes));
      this.radius = Vector2D.getMaximum(new Vector2D(1.5, 1.5), newRadius);

      for (Vector change : changes) {
         int height = this.maxY - this.minY;
         int changeY = change.getBlockY();
         if (changeY > 0) {
            this.minY = this.minY + Math.min(height, changeY);
         } else {
            this.maxY = this.maxY + Math.max(-height, changeY);
         }
      }
   }

   @Override
   public void shift(Vector change) throws RegionOperationException {
      this.center = this.center.add(change.toVector2D());
      int changeY = change.getBlockY();
      this.maxY += changeY;
      this.minY += changeY;
   }

   @Override
   public boolean contains(Vector position) {
      int blockY = position.getBlockY();
      return blockY >= this.minY && blockY <= this.maxY ? position.toVector2D().subtract(this.center).divide(this.radius).lengthSq() <= 1.0 : false;
   }

   public boolean setY(int y) {
      if (!this.hasY) {
         this.minY = y;
         this.maxY = y;
         this.hasY = true;
         return true;
      } else if (y < this.minY) {
         this.minY = y;
         return true;
      } else if (y > this.maxY) {
         this.maxY = y;
         return true;
      } else {
         return false;
      }
   }

   @Override
   public Iterator<BlockVector> iterator() {
      return new FlatRegion3DIterator(this);
   }

   @Override
   public Iterable<Vector2D> asFlatRegion() {
      return new Iterable<Vector2D>() {
         @Override
         public Iterator<Vector2D> iterator() {
            return new FlatRegionIterator(CylinderRegion.this);
         }
      };
   }

   @Override
   public String toString() {
      return this.center + " - " + this.radius + "(" + this.minY + ", " + this.maxY + ")";
   }

   public CylinderRegion clone() {
      return (CylinderRegion)super.clone();
   }

   @Override
   public List<BlockVector2D> polygonize(int maxPoints) {
      return Polygons.polygonizeCylinder(this.center, this.radius, maxPoints);
   }

   public static CylinderRegion createRadius(Extent extent, Vector center, double radius) {
      Preconditions.checkNotNull(extent);
      Preconditions.checkNotNull(center);
      Vector2D radiusVec = new Vector2D(radius, radius);
      int minY = extent.getMinimumPoint().getBlockY();
      int maxY = extent.getMaximumPoint().getBlockY();
      return new CylinderRegion(center, radiusVec, minY, maxY);
   }
}
