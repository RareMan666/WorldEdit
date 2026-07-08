package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockMaterial;
import java.util.Map;
import javax.annotation.Nullable;

public class LegacyBlockRegistry implements BlockRegistry {
   @Nullable
   @Override
   public BaseBlock createFromId(String id) {
      Integer legacyId = BundledBlockData.getInstance().toLegacyId(id);
      return legacyId != null ? this.createFromId(legacyId) : null;
   }

   @Nullable
   @Override
   public BaseBlock createFromId(int id) {
      return new BaseBlock(id);
   }

   @Nullable
   @Override
   public BlockMaterial getMaterial(BaseBlock block) {
      return BundledBlockData.getInstance().getMaterialById(block.getId());
   }

   @Nullable
   @Override
   public Map<String, ? extends State> getStates(BaseBlock block) {
      return BundledBlockData.getInstance().getStatesById(block.getId());
   }
}
