package com.sk89q.worldedit.command.tool.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.function.pattern.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GravityBrush implements Brush {
   private final boolean fullHeight;

   public GravityBrush(boolean fullHeight) {
      this.fullHeight = fullHeight;
   }

   @Override
   public void build(EditSession editSession, Vector position, Pattern pattern, double size) throws MaxChangedBlocksException {
      BaseBlock air = new BaseBlock(0, 0);
      double startY = this.fullHeight ? editSession.getWorld().getMaxY() : position.getBlockY() + size;

      for (double x = position.getBlockX() + size; x > position.getBlockX() - size; x--) {
         for (double z = position.getBlockZ() + size; z > position.getBlockZ() - size; z--) {
            double y = startY;

            List<BaseBlock> blockTypes;
            for (blockTypes = new ArrayList<>(); y > position.getBlockY() - size; y--) {
               Vector pt = new Vector(x, y, z);
               BaseBlock block = editSession.getBlock(pt);
               if (!block.isAir()) {
                  blockTypes.add(block);
                  editSession.setBlock(pt, air);
               }
            }

            Vector pt = new Vector(x, y, z);
            Collections.reverse(blockTypes);

            for (int i = 0; i < blockTypes.size(); pt = pt.add(0, 1, 0)) {
               if (editSession.getBlock(pt).getType() == 0) {
                  editSession.setBlock(pt, blockTypes.get(i++));
               }
            }
         }
      }
   }
}
