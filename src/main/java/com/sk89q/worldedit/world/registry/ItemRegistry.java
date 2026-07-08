package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.blocks.BaseItem;
import javax.annotation.Nullable;

public interface ItemRegistry {
   @Nullable
   BaseItem createFromId(String var1);

   @Nullable
   BaseItem createFromId(int var1);
}
