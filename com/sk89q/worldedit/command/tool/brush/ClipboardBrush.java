package com.sk89q.worldedit.command.tool.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;

public class ClipboardBrush implements Brush {
   private ClipboardHolder holder;
   private boolean ignoreAirBlocks;
   private boolean usingOrigin;

   public ClipboardBrush(ClipboardHolder holder, boolean ignoreAirBlocks, boolean usingOrigin) {
      this.holder = holder;
      this.ignoreAirBlocks = ignoreAirBlocks;
      this.usingOrigin = usingOrigin;
   }

   @Override
   public void build(EditSession editSession, Vector position, Pattern pattern, double size) throws MaxChangedBlocksException {
      Clipboard clipboard = this.holder.getClipboard();
      Region region = clipboard.getRegion();
      Vector centerOffset = region.getCenter().subtract(clipboard.getOrigin());
      Operation operation = this.holder
         .createPaste(editSession, editSession.getWorld().getWorldData())
         .to(this.usingOrigin ? position : position.subtract(centerOffset))
         .ignoreAirBlocks(this.ignoreAirBlocks)
         .build();
      Operations.completeLegacy(operation);
   }
}
