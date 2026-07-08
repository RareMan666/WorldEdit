package com.sk89q.worldedit.extension.input;

import com.sk89q.worldedit.WorldEditException;

public class InputParseException extends WorldEditException {
   public InputParseException(String message) {
      super(message);
   }

   public InputParseException(String message, Throwable cause) {
      super(message, cause);
   }
}
