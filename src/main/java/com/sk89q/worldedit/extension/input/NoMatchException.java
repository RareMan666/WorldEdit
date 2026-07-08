package com.sk89q.worldedit.extension.input;

public class NoMatchException extends InputParseException {
   public NoMatchException(String message) {
      super(message);
   }

   public NoMatchException(String message, Throwable cause) {
      super(message, cause);
   }
}
