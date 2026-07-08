package com.sk89q.worldedit.command.tool;

import com.sk89q.worldedit.WorldEditException;

public class InvalidToolBindException extends WorldEditException {
   private int itemId;

   public InvalidToolBindException(int itemId, String msg) {
      super(msg);
      this.itemId = itemId;
   }

   public int getItemId() {
      return this.itemId;
   }
}
