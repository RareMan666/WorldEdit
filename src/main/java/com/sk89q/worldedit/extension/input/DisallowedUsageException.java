package com.sk89q.worldedit.extension.input;

public class DisallowedUsageException extends InputParseException {
   public DisallowedUsageException(String message) {
      super(message);
   }

   public DisallowedUsageException(String message, Throwable cause) {
      super(message, cause);
   }
}
