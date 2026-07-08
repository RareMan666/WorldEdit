package com.sk89q.worldedit.function.mask;

import com.sk89q.worldedit.Vector2D;
import java.util.Collection;

public class MaskUnion2D extends MaskIntersection2D {
   public MaskUnion2D(Collection<Mask2D> masks) {
      super(masks);
   }

   public MaskUnion2D(Mask2D... mask) {
      super(mask);
   }

   @Override
   public boolean test(Vector2D vector) {
      for (Mask2D mask : this.getMasks()) {
         if (mask.test(vector)) {
            return true;
         }
      }

      return false;
   }
}
