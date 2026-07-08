package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;

@Deprecated
public class ExistingBlockMask extends AbstractMask {
   @Override
   public boolean matches(EditSession editSession, Vector position) {
      return editSession.getBlockType(position) != 0;
   }
}
