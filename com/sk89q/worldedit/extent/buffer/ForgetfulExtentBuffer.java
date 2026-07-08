package com.sk89q.worldedit.extent.buffer;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.Masks;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.AbstractRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionOperationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class ForgetfulExtentBuffer extends AbstractDelegateExtent implements Pattern {
   private static final BaseBlock AIR = new BaseBlock(0);
   private final Map<BlockVector, BaseBlock> buffer = new LinkedHashMap<>();
   private final Mask mask;
   private Vector min = null;
   private Vector max = null;

   public ForgetfulExtentBuffer(Extent delegate) {
      this(delegate, Masks.alwaysTrue());
   }

   public ForgetfulExtentBuffer(Extent delegate, Mask mask) {
      super(delegate);
      Preconditions.checkNotNull(delegate);
      Preconditions.checkNotNull(mask);
      this.mask = mask;
   }

   @Override
   public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
      if (this.min == null) {
         this.min = location;
      } else {
         this.min = Vector.getMinimum(this.min, location);
      }

      if (this.max == null) {
         this.max = location;
      } else {
         this.max = Vector.getMaximum(this.max, location);
      }

      BlockVector blockVector = location.toBlockVector();
      if (this.mask.test(blockVector)) {
         this.buffer.put(blockVector, block);
         return true;
      } else {
         return this.getExtent().setBlock(location, block);
      }
   }

   @Override
   public BaseBlock apply(Vector pos) {
      BaseBlock block = this.buffer.get(pos.toBlockVector());
      return block != null ? block : AIR;
   }

   public Region asRegion() {
      return new AbstractRegion(null) {
         @Override
         public Vector getMinimumPoint() {
            return ForgetfulExtentBuffer.this.min != null ? ForgetfulExtentBuffer.this.min : new Vector();
         }

         @Override
         public Vector getMaximumPoint() {
            return ForgetfulExtentBuffer.this.max != null ? ForgetfulExtentBuffer.this.max : new Vector();
         }

         @Override
         public void expand(Vector... changes) throws RegionOperationException {
            throw new UnsupportedOperationException("Cannot change the size of this region");
         }

         @Override
         public void contract(Vector... changes) throws RegionOperationException {
            throw new UnsupportedOperationException("Cannot change the size of this region");
         }

         @Override
         public boolean contains(Vector position) {
            return ForgetfulExtentBuffer.this.buffer.containsKey(position.toBlockVector());
         }

         @Override
         public Iterator<BlockVector> iterator() {
            return ForgetfulExtentBuffer.this.buffer.keySet().iterator();
         }
      };
   }
}
