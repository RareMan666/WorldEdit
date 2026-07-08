package com.sk89q.worldedit.util.command.argument;

public class MissingArgumentException extends ArgumentException {
   public MissingArgumentException() {
   }

   public MissingArgumentException(String message) {
      super(message);
   }

   public MissingArgumentException(String message, Throwable cause) {
      super(message, cause);
   }

   public MissingArgumentException(Throwable cause) {
      super(cause);
   }
}
