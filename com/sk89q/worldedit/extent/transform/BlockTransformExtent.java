package com.sk89q.worldedit.extent.transform;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.world.registry.BlockRegistry;
import com.sk89q.worldedit.world.registry.State;
import com.sk89q.worldedit.world.registry.StateValue;
import java.util.Map;
import javax.annotation.Nullable;

public class BlockTransformExtent extends AbstractDelegateExtent {
   private static final double RIGHT_ANGLE = Math.toRadians(90.0);
   private final Transform transform;
   private final BlockRegistry blockRegistry;

   public BlockTransformExtent(Extent extent, Transform transform, BlockRegistry blockRegistry) {
      super(extent);
      Preconditions.checkNotNull(transform);
      Preconditions.checkNotNull(blockRegistry);
      this.transform = transform;
      this.blockRegistry = blockRegistry;
   }

   public Transform getTransform() {
      return this.transform;
   }

   private BaseBlock transformBlock(BaseBlock block, boolean reverse) {
      transform(block, reverse ? this.transform.inverse() : this.transform, this.blockRegistry);
      return block;
   }

   @Override
   public BaseBlock getBlock(Vector position) {
      return this.transformBlock(super.getBlock(position), false);
   }

   @Override
   public BaseBlock getLazyBlock(Vector position) {
      return this.transformBlock(super.getLazyBlock(position), false);
   }

   @Override
   public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
      return super.setBlock(location, this.transformBlock(new BaseBlock(block), true));
   }

   public static BaseBlock transform(BaseBlock block, Transform transform, BlockRegistry registry) {
      return transform(block, transform, registry, block);
   }

   private static BaseBlock transform(BaseBlock block, Transform transform, BlockRegistry registry, BaseBlock changedBlock) {
      Preconditions.checkNotNull(block);
      Preconditions.checkNotNull(transform);
      Preconditions.checkNotNull(registry);
      Map<String, ? extends State> states = registry.getStates(block);
      if (states == null) {
         return changedBlock;
      } else {
         for (State state : states.values()) {
            if (state.hasDirection()) {
               StateValue value = state.getValue(block);
               if (value != null && value.getDirection() != null) {
                  StateValue newValue = getNewStateValue(state, transform, value.getDirection());
                  if (newValue != null) {
                     newValue.set(changedBlock);
                  }
               }
            }
         }

         return changedBlock;
      }
   }

   @Nullable
   private static StateValue getNewStateValue(State state, Transform transform, Vector oldDirection) {
      Vector newDirection = transform.apply(oldDirection).subtract(transform.apply(Vector.ZERO)).normalize();
      StateValue newValue = null;
      double closest = -2.0;
      boolean found = false;

      for (StateValue v : state.valueMap().values()) {
         if (v.getDirection() != null) {
            double dot = v.getDirection().normalize().dot(newDirection);
            if (dot >= closest) {
               closest = dot;
               newValue = v;
               found = true;
            }
         }
      }

      return found ? newValue : null;
   }
}
