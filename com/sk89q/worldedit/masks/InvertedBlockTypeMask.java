package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import java.util.Set;

@Deprecated
public class InvertedBlockTypeMask extends BlockTypeMask {
   public InvertedBlockTypeMask() {
   }

   public InvertedBlockTypeMask(Set<Integer> types) {
      super(types);
   }

   public InvertedBlockTypeMask(int type) {
      super(type);
   }

   @Override
   public boolean matches(EditSession editSession, Vector position) {
      return !super.matches(editSession, position);
   }
}
