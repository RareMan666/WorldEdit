package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import javax.annotation.Nullable;

public interface StateValue {
   boolean isSet(BaseBlock var1);

   boolean set(BaseBlock var1);

   @Nullable
   Vector getDirection();
}
