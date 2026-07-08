package com.sk89q.worldedit.function.entity;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.CompoundTagBuilder;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.EntityFunction;
import com.sk89q.worldedit.internal.helper.MCDirections;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.Location;

public class ExtentEntityCopy implements EntityFunction {
   private final Extent destination;
   private final Vector from;
   private final Vector to;
   private final Transform transform;
   private boolean removing;

   public ExtentEntityCopy(Vector from, Extent destination, Vector to, Transform transform) {
      Preconditions.checkNotNull(from);
      Preconditions.checkNotNull(destination);
      Preconditions.checkNotNull(to);
      Preconditions.checkNotNull(transform);
      this.destination = destination;
      this.from = from;
      this.to = to;
      this.transform = transform;
   }

   public boolean isRemoving() {
      return this.removing;
   }

   public void setRemoving(boolean removing) {
      this.removing = removing;
   }

   @Override
   public boolean apply(Entity entity) throws WorldEditException {
      BaseEntity state = entity.getState();
      if (state != null) {
         Location location = entity.getLocation();
         Vector pivot = this.from.round().add(0.5, 0.5, 0.5);
         Vector newPosition = this.transform.apply(location.toVector().subtract(pivot));
         Vector newDirection = this.transform.isIdentity()
            ? entity.getLocation().getDirection()
            : this.transform.apply(location.getDirection()).subtract(this.transform.apply(Vector.ZERO)).normalize();
         Location newLocation = new Location(this.destination, newPosition.add(this.to.round().add(0.5, 0.5, 0.5)), newDirection);
         state = this.transformNbtData(state);
         boolean success = this.destination.createEntity(newLocation, state) != null;
         if (this.isRemoving() && success) {
            entity.remove();
         }

         return success;
      } else {
         return false;
      }
   }

   private BaseEntity transformNbtData(BaseEntity state) {
      CompoundTag tag = state.getNbtData();
      if (tag != null) {
         boolean hasTilePosition = tag.containsKey("TileX") && tag.containsKey("TileY") && tag.containsKey("TileZ");
         boolean hasDirection = tag.containsKey("Direction");
         boolean hasLegacyDirection = tag.containsKey("Dir");
         boolean hasFacing = tag.containsKey("Facing");
         if (hasTilePosition) {
            Vector tilePosition = new Vector(tag.asInt("TileX"), tag.asInt("TileY"), tag.asInt("TileZ"));
            Vector newTilePosition = this.transform.apply(tilePosition.subtract(this.from)).add(this.to);
            CompoundTagBuilder builder = tag.createBuilder()
               .putInt("TileX", newTilePosition.getBlockX())
               .putInt("TileY", newTilePosition.getBlockY())
               .putInt("TileZ", newTilePosition.getBlockZ());
            if (hasDirection || hasLegacyDirection || hasFacing) {
               int d;
               if (hasDirection) {
                  d = tag.asInt("Direction");
               } else if (hasLegacyDirection) {
                  d = MCDirections.fromLegacyHanging((byte)tag.asInt("Dir"));
               } else {
                  d = tag.asInt("Facing");
               }

               Direction direction = MCDirections.fromHanging(d);
               if (direction != null) {
                  Vector vector = this.transform.apply(direction.toVector()).subtract(this.transform.apply(Vector.ZERO)).normalize();
                  Direction newDirection = Direction.findClosest(vector, Direction.Flag.CARDINAL);
                  if (newDirection != null) {
                     byte hangingByte = (byte)MCDirections.toHanging(newDirection);
                     builder.putByte("Direction", hangingByte);
                     builder.putByte("Facing", hangingByte);
                     builder.putByte("Dir", MCDirections.toLegacyHanging(MCDirections.toHanging(newDirection)));
                  }
               }
            }

            return new BaseEntity(state.getTypeId(), builder.build());
         }
      }

      return state;
   }
}
