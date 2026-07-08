package com.sk89q.worldedit;

import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.world.AbstractWorld;

@Deprecated
public abstract class LocalWorld extends AbstractWorld {
   @Override
   public BaseBlock getLazyBlock(Vector position) {
      return this.getBlock(position);
   }

   @Override
   public boolean generateTree(TreeGenerator.TreeType type, EditSession editSession, Vector pt) throws MaxChangedBlocksException {
      switch (type) {
         case BIG_TREE:
            return this.generateBigTree(editSession, pt);
         case BIRCH:
            return this.generateBirchTree(editSession, pt);
         case REDWOOD:
            return this.generateRedwoodTree(editSession, pt);
         case TALL_REDWOOD:
            return this.generateTallRedwoodTree(editSession, pt);
         case TREE:
         default:
            return this.generateTree(editSession, pt);
      }
   }

   @Override
   public boolean generateTree(EditSession editSession, Vector pt) throws MaxChangedBlocksException {
      return false;
   }

   @Override
   public boolean generateBigTree(EditSession editSession, Vector pt) throws MaxChangedBlocksException {
      return false;
   }

   @Override
   public boolean generateBirchTree(EditSession editSession, Vector pt) throws MaxChangedBlocksException {
      return false;
   }

   @Override
   public boolean generateRedwoodTree(EditSession editSession, Vector pt) throws MaxChangedBlocksException {
      return false;
   }

   @Override
   public boolean generateTallRedwoodTree(EditSession editSession, Vector pt) throws MaxChangedBlocksException {
      return false;
   }
}
