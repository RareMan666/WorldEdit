package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.entity.BaseEntity;
import javax.annotation.Nullable;

public interface EntityRegistry {
   @Nullable
   BaseEntity createFromId(String var1);
}
