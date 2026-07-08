package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.entity.BaseEntity;
import javax.annotation.Nullable;

public class NullEntityRegistry implements EntityRegistry {
   @Nullable
   @Override
   public BaseEntity createFromId(String id) {
      return null;
   }
}
