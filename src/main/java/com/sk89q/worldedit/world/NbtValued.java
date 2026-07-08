package com.sk89q.worldedit.world;

import com.sk89q.jnbt.CompoundTag;
import javax.annotation.Nullable;

public interface NbtValued {
   boolean hasNbtData();

   @Nullable
   CompoundTag getNbtData();

   void setNbtData(@Nullable CompoundTag var1);
}
