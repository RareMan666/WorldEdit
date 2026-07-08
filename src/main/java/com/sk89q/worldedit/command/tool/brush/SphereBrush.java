package com.sk89q.worldedit.command.tool.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.Patterns;

public class SphereBrush implements Brush {
   @Override
   public void build(EditSession editSession, Vector position, Pattern pattern, double size) throws MaxChangedBlocksException {
      if (pattern == null) {
         pattern = new BlockPattern(new BaseBlock(4));
      }

      editSession.makeSphere(position, Patterns.wrap(pattern), size, size, size, true);
   }
}
