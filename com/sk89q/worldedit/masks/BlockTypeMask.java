package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.blocks.BaseBlock;
import java.util.Set;

@Deprecated
public class BlockTypeMask extends BlockMask {
   public BlockTypeMask() {
   }

   public BlockTypeMask(Set<Integer> types) {
      for (int type : types) {
         this.add(type);
      }
   }

   public BlockTypeMask(int type) {
      this();
      this.add(type);
   }

   public void add(int type) {
      this.add(new BaseBlock(type));
   }
}
