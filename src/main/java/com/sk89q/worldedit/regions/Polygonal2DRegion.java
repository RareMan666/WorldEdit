package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.regions.iterator.FlatRegion3DIterator;
import com.sk89q.worldedit.regions.iterator.FlatRegionIterator;
import com.sk89q.worldedit.world.World;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Polygonal2DRegion extends AbstractRegion implements FlatRegion {
   private List<BlockVector2D> points;
   private Vector2D min;
   private Vector2D max;
   private int minY;
   private int maxY;
   private boolean hasY = false;

   public Polygonal2DRegion() {
      this((World)null);
   }

   @Deprecated
   public Polygonal2DRegion(LocalWorld world) {
      this((World)world);
   }

   public Polygonal2DRegion(World world) {
      this(world, Collections.emptyList(), 0, 0);
      this.hasY = false;
   }

   @Deprecated
   public Polygonal2DRegion(LocalWorld world, List<BlockVector2D> points, int minY, int maxY) {
      this((World)world, points, minY, maxY);
   }

   public Polygonal2DRegion(World world, List<BlockVector2D> points, int minY, int maxY) {
      super(world);
      this.points = new ArrayList<>(points);
      this.minY = minY;
      this.maxY = maxY;
      this.hasY = true;
      this.recalculate();
   }

   public Polygonal2DRegion(Polygonal2DRegion region) {
      this(region.world, region.points, region.minY, region.maxY);
      this.hasY = region.hasY;
   }

   public List<BlockVector2D> getPoints() {
      return Collections.unmodifiableList(this.points);
   }

   protected void recalculate() {
      if (this.points.isEmpty()) {
         this.min = new Vector2D(0, 0);
         this.minY = 0;
         this.max = new Vector2D(0, 0);
         this.maxY = 0;
      } else {
         int minX = this.points.get(0).getBlockX();
         int minZ = this.points.get(0).getBlockZ();
         int maxX = this.points.get(0).getBlockX();
         int maxZ = this.points.get(0).getBlockZ();

         for (BlockVector2D v : this.points) {
            int x = v.getBlockX();
            int z = v.getBlockZ();
            if (x < minX) {
               minX = x;
            }

            if (z < minZ) {
               minZ = z;
            }

            if (x > maxX) {
               maxX = x;
            }

            if (z > maxZ) {
               maxZ = z;
            }
         }

         int oldMinY = this.minY;
         int oldMaxY = this.maxY;
         this.minY = Math.min(oldMinY, oldMaxY);
         this.maxY = Math.max(oldMinY, oldMaxY);
         this.minY = Math.min(Math.max(0, this.minY), this.world == null ? 255 : this.world.getMaxY());
         this.maxY = Math.min(Math.max(0, this.maxY), this.world == null ? 255 : this.world.getMaxY());
         this.min = new Vector2D(minX, minZ);
         this.max = new Vector2D(maxX, maxZ);
      }
   }

   public void addPoint(Vector2D position) {
      this.points.add(position.toBlockVector2D());
      this.recalculate();
   }

   public void addPoint(BlockVector2D position) {
      this.points.add(position);
      this.recalculate();
   }

   public void addPoint(Vector position) {
      this.points.add(new BlockVector2D(position.getBlockX(), position.getBlockZ()));
      this.recalculate();
   }

   @Override
   public int getMinimumY() {
      return this.minY;
   }

   @Deprecated
   public int getMininumY() {
      return this.minY;
   }

   public void setMinimumY(int y) {
      this.hasY = true;
      this.minY = y;
      this.recalculate();
   }

   @Override
   public int getMaximumY() {
      return this.maxY;
   }

   public void setMaximumY(int y) {
      this.hasY = true;
      this.maxY = y;
      this.recalculate();
   }

   @Override
   public Vector getMinimumPoint() {
      return this.min.toVector(this.minY);
   }

   @Override
   public Vector getMaximumPoint() {
      return this.max.toVector(this.maxY);
   }

   @Override
   public int getArea() {
      double area = 0.0;
      int j = this.points.size() - 1;

      for (int i = 0; i < this.points.size(); j = i++) {
         area += (this.points.get(j).getBlockX() + this.points.get(i).getBlockX()) * (this.points.get(j).getBlockZ() - this.points.get(i).getBlockZ());
      }

      return (int)Math.floor(Math.abs(area * 0.5) * (this.maxY - this.minY + 1));
   }

   @Override
   public int getWidth() {
      return this.max.getBlockX() - this.min.getBlockX() + 1;
   }

   @Override
   public int getHeight() {
      return this.maxY - this.minY + 1;
   }

   @Override
   public int getLength() {
      return this.max.getBlockZ() - this.min.getBlockZ() + 1;
   }

   @Override
   public void expand(Vector... changes) throws RegionOperationException {
      for (Vector change : changes) {
         if (change.getBlockX() != 0 || change.getBlockZ() != 0) {
            throw new RegionOperationException("Polygons can only be expanded vertically.");
         }
      }

      for (Vector changex : changes) {
         int changeY = changex.getBlockY();
         if (changeY > 0) {
            this.maxY += changeY;
         } else {
            this.minY += changeY;
         }
      }

      this.recalculate();
   }

   @Override
   public void contract(Vector... changes) throws RegionOperationException {
      for (Vector change : changes) {
         if (change.getBlockX() != 0 || change.getBlockZ() != 0) {
            throw new RegionOperationException("Polygons can only be contracted vertically.");
         }
      }

      for (Vector changex : changes) {
         int changeY = changex.getBlockY();
         if (changeY > 0) {
            this.minY += changeY;
         } else {
            this.maxY += changeY;
         }
      }

      this.recalculate();
   }

   @Override
   public void shift(Vector change) throws RegionOperationException {
      double changeX = change.getX();
      double changeY = change.getY();
      double changeZ = change.getZ();

      for (int i = 0; i < this.points.size(); i++) {
         BlockVector2D point = this.points.get(i);
         this.points.set(i, new BlockVector2D(point.getX() + changeX, point.getZ() + changeZ));
      }

      this.minY = (int)(this.minY + changeY);
      this.maxY = (int)(this.maxY + changeY);
      this.recalculate();
   }

   @Override
   public boolean contains(Vector position) {
      return contains(this.points, this.minY, this.maxY, position);
   }

   public static boolean contains(List<BlockVector2D> points, int minY, int maxY, Vector pt) {
      if (points.size() < 3) {
         return false;
      } else {
         int targetX = pt.getBlockX();
         int targetY = pt.getBlockY();
         int targetZ = pt.getBlockZ();
         if (targetY >= minY && targetY <= maxY) {
            boolean inside = false;
            int npoints = points.size();
            int xOld = points.get(npoints - 1).getBlockX();
            int zOld = points.get(npoints - 1).getBlockZ();

            for (int i = 0; i < npoints; i++) {
               int xNew = points.get(i).getBlockX();
               int zNew = points.get(i).getBlockZ();
               if (xNew == targetX && zNew == targetZ) {
                  return true;
               }

               int x1;
               int x2;
               int z1;
               int z2;
               if (xNew > xOld) {
                  x1 = xOld;
                  x2 = xNew;
                  z1 = zOld;
                  z2 = zNew;
               } else {
                  x1 = xNew;
                  x2 = xOld;
                  z1 = zNew;
                  z2 = zOld;
               }

               if (x1 <= targetX && targetX <= x2) {
                  long crossproduct = ((long)targetZ - z1) * (x2 - x1) - ((long)z2 - z1) * (targetX - x1);
                  if (crossproduct == 0L) {
                     if (z1 <= targetZ == targetZ <= z2) {
                        return true;
                     }
                  } else if (crossproduct < 0L && x1 != targetX) {
                     inside = !inside;
                  }
               }

               xOld = xNew;
               zOld = zNew;
            }

            return inside;
         } else {
            return false;
         }
      }
   }

   public int size() {
      return this.points.size();
   }

   public boolean expandY(int y) {
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
            return new FlatRegionIterator(Polygonal2DRegion.this);
         }
      };
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      List<BlockVector2D> pts = this.getPoints();
      Iterator<BlockVector2D> it = pts.iterator();

      while (it.hasNext()) {
         BlockVector2D current = it.next();
         sb.append("(").append(current.getBlockX()).append(", ").append(current.getBlockZ()).append(")");
         if (it.hasNext()) {
            sb.append(" - ");
         }
      }

      sb.append(" * (").append(this.minY).append(" - ").append(this.maxY).append(")");
      return sb.toString();
   }

   public Polygonal2DRegion clone() {
      Polygonal2DRegion clone = (Polygonal2DRegion)super.clone();
      clone.points = new ArrayList<>(this.points);
      return clone;
   }

   @Override
   public List<BlockVector2D> polygonize(int maxPoints) {
      if (maxPoints >= 0 && maxPoints < this.points.size()) {
         throw new IllegalArgumentException("Cannot polygonize a this Polygonal2DRegion into the amount of points given.");
      } else {
         return this.points;
      }
   }
}
