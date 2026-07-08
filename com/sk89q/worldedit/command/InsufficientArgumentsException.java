package com.sk89q.worldedit.command;

import com.sk89q.worldedit.WorldEditException;

public class InsufficientArgumentsException extends WorldEditException {
   public InsufficientArgumentsException(String error) {
      super(error);
   }
}
