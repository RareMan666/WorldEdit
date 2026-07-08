package com.sk89q.worldedit.function.mask;

import com.sk89q.worldedit.Vector;
import javax.annotation.Nullable;

public interface Mask {
   boolean test(Vector var1);

   @Nullable
   Mask2D toMask2D();
}
