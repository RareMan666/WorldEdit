package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockMaterial;
import java.util.Map;
import javax.annotation.Nullable;

public interface BlockRegistry {
   @Nullable
   BaseBlock createFromId(String var1);

   @Nullable
   BaseBlock createFromId(int var1);

   @Nullable
   BlockMaterial getMaterial(BaseBlock var1);

   @Nullable
   Map<String, ? extends State> getStates(BaseBlock var1);
}
