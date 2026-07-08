package com.sk89q.worldedit.util;

import com.google.common.base.Preconditions;
import javax.annotation.Nullable;

public final class GuavaUtil {
   private GuavaUtil() {
   }

   public static <T> T firstNonNull(@Nullable T first, @Nullable T second) {
      return (T)(first != null ? first : Preconditions.checkNotNull(second));
   }
}
