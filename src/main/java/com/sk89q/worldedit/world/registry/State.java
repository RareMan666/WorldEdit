package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.blocks.BaseBlock;
import java.util.Map;
import javax.annotation.Nullable;

public interface State {
   Map<String, ? extends StateValue> valueMap();

   @Nullable
   StateValue getValue(BaseBlock var1);

   boolean hasDirection();
}
