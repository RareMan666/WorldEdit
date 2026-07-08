package com.sk89q.worldedit.function.pattern;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.clipboard.Clipboard;

public class ClipboardPattern extends AbstractPattern {
   private final Clipboard clipboard;
   private final Vector size;

   public ClipboardPattern(Clipboard clipboard) {
      Preconditions.checkNotNull(clipboard);
      this.clipboard = clipboard;
      this.size = clipboard.getMaximumPoint().subtract(clipboard.getMinimumPoint()).add(1, 1, 1);
   }

   @Override
   public BaseBlock apply(Vector position) {
      int xp = Math.abs(position.getBlockX()) % this.size.getBlockX();
      int yp = Math.abs(position.getBlockY()) % this.size.getBlockY();
      int zp = Math.abs(position.getBlockZ()) % this.size.getBlockZ();
      return this.clipboard.getBlock(this.clipboard.getMinimumPoint().add(new Vector(xp, yp, zp)));
   }
}
