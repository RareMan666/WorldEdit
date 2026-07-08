package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.blocks.BaseItem;
import javax.annotation.Nullable;

public class NullItemRegistry implements ItemRegistry {
   @Nullable
   @Override
   public BaseItem createFromId(String id) {
      return null;
   }

   @Nullable
   @Override
   public BaseItem createFromId(int id) {
      return null;
   }
}
