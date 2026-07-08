package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import javax.annotation.Nullable;

public class OffsetMask extends AbstractMask {
   private Mask mask;
   private Vector offset;

   public OffsetMask(Mask mask, Vector offset) {
      Preconditions.checkNotNull(mask);
      Preconditions.checkNotNull(offset);
      this.mask = mask;
      this.offset = offset;
   }

   public Mask getMask() {
      return this.mask;
   }

   public void setMask(Mask mask) {
      Preconditions.checkNotNull(mask);
      this.mask = mask;
   }

   public Vector getOffset() {
      return this.offset;
   }

   public void setOffset(Vector offset) {
      Preconditions.checkNotNull(offset);
      this.offset = offset;
   }

   @Override
   public boolean test(Vector vector) {
      return this.getMask().test(vector.add(this.offset));
   }

   @Nullable
   @Override
   public Mask2D toMask2D() {
      Mask2D childMask = this.getMask().toMask2D();
      return childMask != null ? new OffsetMask2D(childMask, this.getOffset().toVector2D()) : null;
   }
}
