package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Deprecated
public class BlockMask extends AbstractMask {
   private final Set<BaseBlock> blocks;

   public BlockMask() {
      this.blocks = new HashSet<>();
   }

   public BlockMask(Set<BaseBlock> types) {
      this.blocks = types;
   }

   public BlockMask(BaseBlock... block) {
      this.blocks = new HashSet<>();

      for (BaseBlock b : block) {
         this.add(b);
      }
   }

   public BlockMask(BaseBlock block) {
      this();
      this.add(block);
   }

   public void add(BaseBlock block) {
      this.blocks.add(block);
   }

   public void addAll(Collection<BaseBlock> blocks) {
      blocks.addAll(blocks);
   }

   @Override
   public boolean matches(EditSession editSession, Vector position) {
      BaseBlock block = editSession.getBlock(position);
      return this.blocks.contains(block) || this.blocks.contains(new BaseBlock(block.getType(), -1));
   }
}
