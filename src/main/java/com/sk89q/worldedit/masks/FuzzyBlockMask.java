package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.Blocks;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Deprecated
public class FuzzyBlockMask extends AbstractMask {
   private final Set<BaseBlock> filter;

   public FuzzyBlockMask(Set<BaseBlock> filter) {
      this.filter = filter;
   }

   public FuzzyBlockMask(BaseBlock... block) {
      Set<BaseBlock> filter = new HashSet<>();
      Collections.addAll(filter, block);
      this.filter = filter;
   }

   @Override
   public boolean matches(EditSession editSession, Vector position) {
      BaseBlock compare = new BaseBlock(editSession.getBlockType(position), editSession.getBlockData(position));
      return Blocks.containsFuzzy(this.filter, compare);
   }
}
