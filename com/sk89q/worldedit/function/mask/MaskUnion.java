package com.sk89q.worldedit.function.mask;

import com.sk89q.worldedit.Vector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;

public class MaskUnion extends MaskIntersection {
   public MaskUnion(Collection<Mask> masks) {
      super(masks);
   }

   public MaskUnion(Mask... mask) {
      super(mask);
   }

   @Override
   public boolean test(Vector vector) {
      for (Mask mask : this.getMasks()) {
         if (mask.test(vector)) {
            return true;
         }
      }

      return false;
   }

   @Nullable
   @Override
   public Mask2D toMask2D() {
      List<Mask2D> mask2dList = new ArrayList<>();

      for (Mask mask : this.getMasks()) {
         Mask2D mask2d = mask.toMask2D();
         if (mask2d == null) {
            return null;
         }

         mask2dList.add(mask2d);
      }

      return new MaskUnion2D(mask2dList);
   }
}
