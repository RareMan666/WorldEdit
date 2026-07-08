package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector2D;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MaskIntersection2D implements Mask2D {
   private final Set<Mask2D> masks = new HashSet<>();

   public MaskIntersection2D(Collection<Mask2D> masks) {
      Preconditions.checkNotNull(masks);
      this.masks.addAll(masks);
   }

   public MaskIntersection2D(Mask2D... mask) {
      this(Arrays.asList(Preconditions.checkNotNull(mask)));
   }

   public void add(Collection<Mask2D> masks) {
      Preconditions.checkNotNull(masks);
      this.masks.addAll(masks);
   }

   public void add(Mask2D... mask) {
      this.add(Arrays.asList(Preconditions.checkNotNull(mask)));
   }

   public Collection<Mask2D> getMasks() {
      return this.masks;
   }

   @Override
   public boolean test(Vector2D vector) {
      if (this.masks.isEmpty()) {
         return false;
      } else {
         for (Mask2D mask : this.masks) {
            if (!mask.test(vector)) {
               return false;
            }
         }

         return true;
      }
   }
}
