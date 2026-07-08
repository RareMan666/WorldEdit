package com.sk89q.worldedit.function.generator;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.util.TreeGenerator;

public class ForestGenerator implements RegionFunction {
   private final TreeGenerator treeGenerator;
   private final EditSession editSession;

   public ForestGenerator(EditSession editSession, TreeGenerator treeGenerator) {
      this.editSession = editSession;
      this.treeGenerator = treeGenerator;
   }

   @Override
   public boolean apply(Vector position) throws WorldEditException {
      BaseBlock block = this.editSession.getBlock(position);
      int t = block.getType();
      if (t == 2 || t == 3) {
         this.treeGenerator.generate(this.editSession, position.add(0, 1, 0));
         return true;
      } else if (t == 31 || t == 32 || t == 38 || t == 37) {
         this.editSession.setBlock(position, new BaseBlock(0));
         this.treeGenerator.generate(this.editSession, position);
         return true;
      } else if (t == 78) {
         this.editSession.setBlock(position, new BaseBlock(0));
         return false;
      } else {
         return false;
      }
   }
}
