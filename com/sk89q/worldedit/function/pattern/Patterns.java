package com.sk89q.worldedit.function.pattern;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;

public final class Patterns {
   private Patterns() {
   }

   public static Pattern wrap(final com.sk89q.worldedit.patterns.Pattern pattern) {
      Preconditions.checkNotNull(pattern);
      return new Pattern() {
         @Override
         public BaseBlock apply(Vector position) {
            return pattern.next(position);
         }
      };
   }

   public static com.sk89q.worldedit.patterns.Pattern wrap(final Pattern pattern) {
      Preconditions.checkNotNull(pattern);
      return new com.sk89q.worldedit.patterns.Pattern() {
         @Override
         public BaseBlock next(Vector position) {
            return pattern.apply(position);
         }

         @Override
         public BaseBlock next(int x, int y, int z) {
            return this.next(new Vector(x, y, z));
         }
      };
   }
}
