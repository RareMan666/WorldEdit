package com.sk89q.worldedit.blocks;

import java.util.Collection;

public final class Blocks {
   private Blocks() {
   }

   public static boolean containsFuzzy(Collection<? extends BaseBlock> collection, BaseBlock o) {
      for (BaseBlock b : collection) {
         if (b.equalsFuzzy(o)) {
            return true;
         }
      }

      return false;
   }
}
