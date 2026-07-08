package com.sk89q.worldedit.internal.gson;

public interface ExclusionStrategy {
   boolean shouldSkipField(FieldAttributes var1);

   boolean shouldSkipClass(Class<?> var1);
}
