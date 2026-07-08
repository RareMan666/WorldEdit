package com.sk89q.worldedit.regions;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.world.World;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class CuboidRegion extends AbstractRegion implements FlatRegion {
   private Vector pos1;
   private Vector pos2;

   public CuboidRegion(Vector pos1, Vector pos2) {
      this(null, pos1, pos2);
   }

   @Deprecated
   public CuboidRegion(LocalWorld world, Vector pos1, Vector pos2) {
      this((World)world, pos1, pos2);
   }

   public CuboidRegion(World world, Vector pos1, Vector pos2) {
      super(world);
      Preconditions.checkNotNull(pos1);
      Preconditions.checkNotNull(pos2);
      this.pos1 = pos1;
      this.pos2 = pos2;
      this.recalculate();
   }

   public Vector getPos1() {
      return this.pos1;
   }

   public void setPos1(Vector pos1) {
      this.pos1 = pos1;
   }

   public Vector getPos2() {
      return this.pos2;
   }

   public void setPos2(Vector pos2) {
      this.pos2 = pos2;
   }

   private void recalculate() {
      this.pos1 = this.pos1.clampY(0, this.world == null ? 255 : this.world.getMaxY());
      this.pos2 = this.pos2.clampY(0, this.world == null ? 255 : this.world.getMaxY());
   }

   public Region getFaces() {
      Vector min = this.getMinimumPoint();
      Vector max = this.getMaximumPoint();
      return new RegionIntersection(
         new CuboidRegion(this.pos1.setX(min.getX()), this.pos2.setX(min.getX())),
         new CuboidRegion(this.pos1.setX(max.getX()), this.pos2.setX(max.getX())),
         new CuboidRegion(this.pos1.setZ(min.getZ()), this.pos2.setZ(min.getZ())),
         new CuboidRegion(this.pos1.setZ(max.getZ()), this.pos2.setZ(max.getZ())),
         new CuboidRegion(this.pos1.setY(min.getY()), this.pos2.setY(min.getY())),
         new CuboidRegion(this.pos1.setY(max.getY()), this.pos2.setY(max.getY()))
      );
   }

   public Region getWalls() {
      Vector min = this.getMinimumPoint();
      Vector max = this.getMaximumPoint();
      return new RegionIntersection(
         new CuboidRegion(this.pos1.setX(min.getX()), this.pos2.setX(min.getX())),
         new CuboidRegion(this.pos1.setX(max.getX()), this.pos2.setX(max.getX())),
         new CuboidRegion(this.pos1.setZ(min.getZ()), this.pos2.setZ(min.getZ())),
         new CuboidRegion(this.pos1.setZ(max.getZ()), this.pos2.setZ(max.getZ()))
      );
   }

   @Override
   public Vector getMinimumPoint() {
      return new Vector(
         Math.min(this.pos1.getX(), this.pos2.getX()), Math.min(this.pos1.getY(), this.pos2.getY()), Math.min(this.pos1.getZ(), this.pos2.getZ())
      );
   }

   @Override
   public Vector getMaximumPoint() {
      return new Vector(
         Math.max(this.pos1.getX(), this.pos2.getX()), Math.max(this.pos1.getY(), this.pos2.getY()), Math.max(this.pos1.getZ(), this.pos2.getZ())
      );
   }

   @Override
   public int getMinimumY() {
      return Math.min(this.pos1.getBlockY(), this.pos2.getBlockY());
   }

   @Override
   public int getMaximumY() {
      return Math.max(this.pos1.getBlockY(), this.pos2.getBlockY());
   }

   @Override
   public void expand(Vector... changes) {
      Preconditions.checkNotNull(changes);

      for (Vector change : changes) {
         if (change.getX() > 0.0) {
            if (Math.max(this.pos1.getX(), this.pos2.getX()) == this.pos1.getX()) {
               this.pos1 = this.pos1.add(new Vector(change.getX(), 0.0, 0.0));
            } else {
               this.pos2 = this.pos2.add(new Vector(change.getX(), 0.0, 0.0));
            }
         } else if (Math.min(this.pos1.getX(), this.pos2.getX()) == this.pos1.getX()) {
            this.pos1 = this.pos1.add(new Vector(change.getX(), 0.0, 0.0));
         } else {
            this.pos2 = this.pos2.add(new Vector(change.getX(), 0.0, 0.0));
         }

         if (change.getY() > 0.0) {
            if (Math.max(this.pos1.getY(), this.pos2.getY()) == this.pos1.getY()) {
               this.pos1 = this.pos1.add(new Vector(0.0, change.getY(), 0.0));
            } else {
               this.pos2 = this.pos2.add(new Vector(0.0, change.getY(), 0.0));
            }
         } else if (Math.min(this.pos1.getY(), this.pos2.getY()) == this.pos1.getY()) {
            this.pos1 = this.pos1.add(new Vector(0.0, change.getY(), 0.0));
         } else {
            this.pos2 = this.pos2.add(new Vector(0.0, change.getY(), 0.0));
         }

         if (change.getZ() > 0.0) {
            if (Math.max(this.pos1.getZ(), this.pos2.getZ()) == this.pos1.getZ()) {
               this.pos1 = this.pos1.add(new Vector(0.0, 0.0, change.getZ()));
            } else {
               this.pos2 = this.pos2.add(new Vector(0.0, 0.0, change.getZ()));
            }
         } else if (Math.min(this.pos1.getZ(), this.pos2.getZ()) == this.pos1.getZ()) {
            this.pos1 = this.pos1.add(new Vector(0.0, 0.0, change.getZ()));
         } else {
            this.pos2 = this.pos2.add(new Vector(0.0, 0.0, change.getZ()));
         }
      }

      this.recalculate();
   }

   @Override
   public void contract(Vector... changes) {
      Preconditions.checkNotNull(changes);

      for (Vector change : changes) {
         if (change.getX() < 0.0) {
            if (Math.max(this.pos1.getX(), this.pos2.getX()) == this.pos1.getX()) {
               this.pos1 = this.pos1.add(new Vector(change.getX(), 0.0, 0.0));
            } else {
               this.pos2 = this.pos2.add(new Vector(change.getX(), 0.0, 0.0));
            }
         } else if (Math.min(this.pos1.getX(), this.pos2.getX()) == this.pos1.getX()) {
            this.pos1 = this.pos1.add(new Vector(change.getX(), 0.0, 0.0));
         } else {
            this.pos2 = this.pos2.add(new Vector(change.getX(), 0.0, 0.0));
         }

         if (change.getY() < 0.0) {
            if (Math.max(this.pos1.getY(), this.pos2.getY()) == this.pos1.getY()) {
               this.pos1 = this.pos1.add(new Vector(0.0, change.getY(), 0.0));
            } else {
               this.pos2 = this.pos2.add(new Vector(0.0, change.getY(), 0.0));
            }
         } else if (Math.min(this.pos1.getY(), this.pos2.getY()) == this.pos1.getY()) {
            this.pos1 = this.pos1.add(new Vector(0.0, change.getY(), 0.0));
         } else {
            this.pos2 = this.pos2.add(new Vector(0.0, change.getY(), 0.0));
         }

         if (change.getZ() < 0.0) {
            if (Math.max(this.pos1.getZ(), this.pos2.getZ()) == this.pos1.getZ()) {
               this.pos1 = this.pos1.add(new Vector(0.0, 0.0, change.getZ()));
            } else {
               this.pos2 = this.pos2.add(new Vector(0.0, 0.0, change.getZ()));
            }
         } else if (Math.min(this.pos1.getZ(), this.pos2.getZ()) == this.pos1.getZ()) {
            this.pos1 = this.pos1.add(new Vector(0.0, 0.0, change.getZ()));
         } else {
            this.pos2 = this.pos2.add(new Vector(0.0, 0.0, change.getZ()));
         }
      }

      this.recalculate();
   }

   @Override
   public void shift(Vector change) throws RegionOperationException {
      this.pos1 = this.pos1.add(change);
      this.pos2 = this.pos2.add(change);
      this.recalculate();
   }

   @Override
   public Set<Vector2D> getChunks() {
      Set<Vector2D> chunks = new HashSet<>();
      Vector min = this.getMinimumPoint();
      Vector max = this.getMaximumPoint();

      for (int x = min.getBlockX() >> 4; x <= max.getBlockX() >> 4; x++) {
         for (int z = min.getBlockZ() >> 4; z <= max.getBlockZ() >> 4; z++) {
            chunks.add(new BlockVector2D(x, z));
         }
      }

      return chunks;
   }

   @Override
   public Set<Vector> getChunkCubes() {
      Set<Vector> chunks = new HashSet<>();
      Vector min = this.getMinimumPoint();
      Vector max = this.getMaximumPoint();

      for (int x = min.getBlockX() >> 4; x <= max.getBlockX() >> 4; x++) {
         for (int z = min.getBlockZ() >> 4; z <= max.getBlockZ() >> 4; z++) {
            for (int y = min.getBlockY() >> 4; y <= max.getBlockY() >> 4; y++) {
               chunks.add(new BlockVector(x, y, z));
            }
         }
      }

      return chunks;
   }

   @Override
   public boolean contains(Vector position) {
      double x = position.getX();
      double y = position.getY();
      double z = position.getZ();
      Vector min = this.getMinimumPoint();
      Vector max = this.getMaximumPoint();
      return x >= min.getBlockX() && x <= max.getBlockX() && y >= min.getBlockY() && y <= max.getBlockY() && z >= min.getBlockZ() && z <= max.getBlockZ();
   }

   @Override
   public Iterator<BlockVector> iterator() {
      return new Iterator<BlockVector>() {
         private Vector min = CuboidRegion.this.getMinimumPoint();
         private Vector max = CuboidRegion.this.getMaximumPoint();
         private int nextX = this.min.getBlockX();
         private int nextY = this.min.getBlockY();
         private int nextZ = this.min.getBlockZ();

         @Override
         public boolean hasNext() {
            return this.nextX != Integer.MIN_VALUE;
         }

         public BlockVector next() {
            if (!this.hasNext()) {
               throw new NoSuchElementException();
            } else {
               BlockVector answer = new BlockVector(this.nextX, this.nextY, this.nextZ);
               if (++this.nextX > this.max.getBlockX()) {
                  this.nextX = this.min.getBlockX();
                  if (++this.nextY > this.max.getBlockY()) {
                     this.nextY = this.min.getBlockY();
                     if (++this.nextZ > this.max.getBlockZ()) {
                        this.nextX = Integer.MIN_VALUE;
                     }
                  }
               }

               return answer;
            }
         }

         @Override
         public void remove() {
            throw new UnsupportedOperationException();
         }
      };
   }

   @Override
   public Iterable<Vector2D> asFlatRegion() {
      return new Iterable<Vector2D>() {
         @Override
         public Iterator<Vector2D> iterator() {
            return new Iterator<Vector2D>() {
               private Vector min = CuboidRegion.this.getMinimumPoint();
               private Vector max = CuboidRegion.this.getMaximumPoint();
               private int nextX = this.min.getBlockX();
               private int nextZ = this.min.getBlockZ();

               @Override
               public boolean hasNext() {
                  return this.nextX != Integer.MIN_VALUE;
               }

               public Vector2D next() {
                  if (!this.hasNext()) {
                     throw new NoSuchElementException();
                  } else {
                     Vector2D answer = new Vector2D(this.nextX, this.nextZ);
                     if (++this.nextX > this.max.getBlockX()) {
                        this.nextX = this.min.getBlockX();
                        if (++this.nextZ > this.max.getBlockZ()) {
                           this.nextX = Integer.MIN_VALUE;
                        }
                     }

                     return answer;
                  }
               }

               @Override
               public void remove() {
                  throw new UnsupportedOperationException();
               }
            };
         }
      };
   }

   @Override
   public String toString() {
      return this.getMinimumPoint() + " - " + this.getMaximumPoint();
   }

   public CuboidRegion clone() {
      return (CuboidRegion)super.clone();
   }

   public static CuboidRegion makeCuboid(Region region) {
      Preconditions.checkNotNull(region);
      return new CuboidRegion(region.getMinimumPoint(), region.getMaximumPoint());
   }

   public static CuboidRegion fromCenter(Vector origin, int apothem) {
      Preconditions.checkNotNull(origin);
      Preconditions.checkArgument(apothem >= 0, "apothem => 0 required");
      Vector size = new Vector(1, 1, 1).multiply(apothem);
      return new CuboidRegion(origin.subtract(size), origin.add(size));
   }
}
