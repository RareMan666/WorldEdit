package com.sk89q.worldedit.util.command.argument;

public class ArgumentException extends Exception {
   public ArgumentException() {
   }

   public ArgumentException(String message) {
      super(message);
   }

   public ArgumentException(String message, Throwable cause) {
      super(message, cause);
   }

   public ArgumentException(Throwable cause) {
      super(cause);
   }
}
