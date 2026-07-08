package com.sk89q.worldedit.world.storage;

import com.sk89q.worldedit.world.DataException;

public class InvalidFormatException extends DataException {
   public InvalidFormatException(String msg) {
      super(msg);
   }
}
