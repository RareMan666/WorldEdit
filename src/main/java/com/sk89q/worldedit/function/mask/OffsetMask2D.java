package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector2D;

public class OffsetMask2D extends AbstractMask2D {
   private Mask2D mask;
   private Vector2D offset;

   public OffsetMask2D(Mask2D mask, Vector2D offset) {
      Preconditions.checkNotNull(mask);
      Preconditions.checkNotNull(offset);
      this.mask = mask;
      this.offset = offset;
   }

   public Mask2D getMask() {
      return this.mask;
   }

   public void setMask(Mask2D mask) {
      Preconditions.checkNotNull(mask);
      this.mask = mask;
   }

   public Vector2D getOffset() {
      return this.offset;
   }

   public void setOffset(Vector2D offset) {
      Preconditions.checkNotNull(offset);
      this.offset = offset;
   }

   @Override
   public boolean test(Vector2D vector) {
      return this.getMask().test(vector.add(this.offset));
   }
}
