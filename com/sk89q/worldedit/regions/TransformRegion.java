package com.sk89q.worldedit.regions;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.math.transform.Identity;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.world.World;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class TransformRegion extends AbstractRegion {
   private final Region region;
   private Transform transform = new Identity();

   public TransformRegion(Region region, Transform transform) {
      this(null, region, transform);
   }

   public TransformRegion(@Nullable World world, Region region, Transform transform) {
      super(world);
      Preconditions.checkNotNull(region);
      Preconditions.checkNotNull(transform);
      this.region = region;
      this.transform = transform;
   }

   public Region getRegion() {
      return this.region;
   }

   public Transform getTransform() {
      return this.transform;
   }

   public void setTransform(Transform transform) {
      Preconditions.checkNotNull(transform);
      this.transform = transform;
   }

   @Override
   public Vector getMinimumPoint() {
      return this.transform.apply(this.region.getMinimumPoint());
   }

   @Override
   public Vector getMaximumPoint() {
      return this.transform.apply(this.region.getMaximumPoint());
   }

   @Override
   public Vector getCenter() {
      return this.transform.apply(this.region.getCenter());
   }

   @Override
   public int getArea() {
      return this.region.getArea();
   }

   @Override
   public int getWidth() {
      return this.getMaximumPoint().subtract(this.getMinimumPoint()).getBlockX() + 1;
   }

   @Override
   public int getHeight() {
      return this.getMaximumPoint().subtract(this.getMinimumPoint()).getBlockY() + 1;
   }

   @Override
   public int getLength() {
      return this.getMaximumPoint().subtract(this.getMinimumPoint()).getBlockZ() + 1;
   }

   @Override
   public void expand(Vector... changes) throws RegionOperationException {
      throw new RegionOperationException("Can't expand a TransformedRegion");
   }

   @Override
   public void contract(Vector... changes) throws RegionOperationException {
      throw new RegionOperationException("Can't contract a TransformedRegion");
   }

   @Override
   public void shift(Vector change) throws RegionOperationException {
      throw new RegionOperationException("Can't change a TransformedRegion");
   }

   @Override
   public boolean contains(Vector position) {
      return this.region.contains(this.transform.inverse().apply(position));
   }

   @Override
   public List<BlockVector2D> polygonize(int maxPoints) {
      List<BlockVector2D> origPoints = this.region.polygonize(maxPoints);
      List<BlockVector2D> transformedPoints = new ArrayList<>();

      for (BlockVector2D vector : origPoints) {
         transformedPoints.add(this.transform.apply(vector.toVector(0.0)).toVector2D().toBlockVector2D());
      }

      return transformedPoints;
   }

   @Override
   public Iterator<BlockVector> iterator() {
      final Iterator<BlockVector> it = this.region.iterator();
      return new Iterator<BlockVector>() {
         @Override
         public boolean hasNext() {
            return it.hasNext();
         }

         public BlockVector next() {
            BlockVector next = it.next();
            return next != null ? TransformRegion.this.transform.apply(next).toBlockVector() : null;
         }

         @Override
         public void remove() {
            it.remove();
         }
      };
   }
}
