package com.sk89q.worldedit.internal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Constants {
   public static final List<String> NO_COPY_ENTITY_NBT_FIELDS = Collections.unmodifiableList(
      Arrays.asList("UUIDLeast", "UUIDMost", "WorldUUIDLeast", "WorldUUIDMost", "PersistentIDMSB", "PersistentIDLSB")
   );

   private Constants() {
   }
}
