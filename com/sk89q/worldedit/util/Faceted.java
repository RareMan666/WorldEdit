package com.sk89q.worldedit.util;

import javax.annotation.Nullable;

public interface Faceted {
   @Nullable
   <T> T getFacet(Class<? extends T> var1);
}
