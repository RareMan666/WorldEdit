package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;

public class MaskIntersection extends AbstractMask {
   private final Set<Mask> masks = new HashSet<>();

   public MaskIntersection(Collection<Mask> masks) {
      Preconditions.checkNotNull(masks);
      this.masks.addAll(masks);
   }

   public MaskIntersection(Mask... mask) {
      this(Arrays.asList(Preconditions.checkNotNull(mask)));
   }

   public void add(Collection<Mask> masks) {
      Preconditions.checkNotNull(masks);
      this.masks.addAll(masks);
   }

   public void add(Mask... mask) {
      this.add(Arrays.asList(Preconditions.checkNotNull(mask)));
   }

   public Collection<Mask> getMasks() {
      return this.masks;
   }

   @Override
   public boolean test(Vector vector) {
      if (this.masks.isEmpty()) {
         return false;
      } else {
         for (Mask mask : this.masks) {
            if (!mask.test(vector)) {
               return false;
            }
         }

         return true;
      }
   }

   @Nullable
   @Override
   public Mask2D toMask2D() {
      List<Mask2D> mask2dList = new ArrayList<>();

      for (Mask mask : this.masks) {
         Mask2D mask2d = mask.toMask2D();
         if (mask2d == null) {
            return null;
         }

         mask2dList.add(mask2d);
      }

      return new MaskIntersection2D(mask2dList);
   }
}
