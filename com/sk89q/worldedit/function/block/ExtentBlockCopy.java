package com.sk89q.worldedit.function.block;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.CompoundTagBuilder;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.internal.helper.MCDirections;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.util.Direction;

public class ExtentBlockCopy implements RegionFunction {
   private final Extent source;
   private final Extent destination;
   private final Vector from;
   private final Vector to;
   private final Transform transform;

   public ExtentBlockCopy(Extent source, Vector from, Extent destination, Vector to, Transform transform) {
      Preconditions.checkNotNull(source);
      Preconditions.checkNotNull(from);
      Preconditions.checkNotNull(destination);
      Preconditions.checkNotNull(to);
      Preconditions.checkNotNull(transform);
      this.source = source;
      this.from = from;
      this.destination = destination;
      this.to = to;
      this.transform = transform;
   }

   @Override
   public boolean apply(Vector position) throws WorldEditException {
      BaseBlock block = this.source.getBlock(position);
      Vector orig = position.subtract(this.from);
      Vector transformed = this.transform.apply(orig);
      block = this.transformNbtData(block);
      return this.destination.setBlock(transformed.add(this.to), block);
   }

   private BaseBlock transformNbtData(BaseBlock state) {
      CompoundTag tag = state.getNbtData();
      if (tag != null && tag.containsKey("Rot")) {
         int rot = tag.asInt("Rot");
         Direction direction = MCDirections.fromRotation(rot);
         if (direction != null) {
            Vector vector = this.transform.apply(direction.toVector()).subtract(this.transform.apply(Vector.ZERO)).normalize();
            Direction newDirection = Direction.findClosest(vector, Direction.Flag.CARDINAL | Direction.Flag.ORDINAL | Direction.Flag.SECONDARY_ORDINAL);
            if (newDirection != null) {
               CompoundTagBuilder builder = tag.createBuilder();
               builder.putByte("Rot", (byte)MCDirections.toRotation(newDirection));
               return new BaseBlock(state.getId(), state.getData(), builder.build());
            }
         }
      }

      return state;
   }
}
