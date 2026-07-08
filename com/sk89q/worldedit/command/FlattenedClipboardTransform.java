package com.sk89q.worldedit.command;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.transform.BlockTransformExtent;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.math.transform.CombinedTransform;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.registry.WorldData;

class FlattenedClipboardTransform {
   private final Clipboard original;
   private final Transform transform;
   private final WorldData worldData;

   private FlattenedClipboardTransform(Clipboard original, Transform transform, WorldData worldData) {
      Preconditions.checkNotNull(original);
      Preconditions.checkNotNull(transform);
      Preconditions.checkNotNull(worldData);
      this.original = original;
      this.transform = transform;
      this.worldData = worldData;
   }

   public Region getTransformedRegion() {
      Region region = this.original.getRegion();
      Vector minimum = region.getMinimumPoint();
      Vector maximum = region.getMaximumPoint();
      Transform transformAround = new CombinedTransform(
         new AffineTransform().translate(this.original.getOrigin().multiply(-1)), this.transform, new AffineTransform().translate(this.original.getOrigin())
      );
      Vector[] corners = new Vector[]{
         minimum,
         maximum,
         minimum.setX(maximum.getX()),
         minimum.setY(maximum.getY()),
         minimum.setZ(maximum.getZ()),
         maximum.setX(minimum.getX()),
         maximum.setY(minimum.getY()),
         maximum.setZ(minimum.getZ())
      };

      for (int i = 0; i < corners.length; i++) {
         corners[i] = transformAround.apply(corners[i]);
      }

      Vector newMinimum = corners[0];
      Vector newMaximum = corners[0];

      for (int i = 1; i < corners.length; i++) {
         newMinimum = Vector.getMinimum(newMinimum, corners[i]);
         newMaximum = Vector.getMaximum(newMaximum, corners[i]);
      }

      newMinimum = newMinimum.setX(Math.floor(newMinimum.getX()));
      newMinimum = newMinimum.setY(Math.floor(newMinimum.getY()));
      newMinimum = newMinimum.setZ(Math.floor(newMinimum.getZ()));
      newMaximum = newMaximum.setX(Math.ceil(newMaximum.getX()));
      newMaximum = newMaximum.setY(Math.ceil(newMaximum.getY()));
      newMaximum = newMaximum.setZ(Math.ceil(newMaximum.getZ()));
      return new CuboidRegion(newMinimum, newMaximum);
   }

   public Operation copyTo(Extent target) {
      BlockTransformExtent extent = new BlockTransformExtent(this.original, this.transform, this.worldData.getBlockRegistry());
      ForwardExtentCopy copy = new ForwardExtentCopy(extent, this.original.getRegion(), this.original.getOrigin(), target, this.original.getOrigin());
      copy.setTransform(this.transform);
      return copy;
   }

   public static FlattenedClipboardTransform transform(Clipboard original, Transform transform, WorldData worldData) {
      return new FlattenedClipboardTransform(original, transform, worldData);
   }
}
