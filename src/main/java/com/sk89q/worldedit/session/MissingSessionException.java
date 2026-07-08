package com.sk89q.worldedit.session;

public class MissingSessionException extends Exception {
   public MissingSessionException() {
   }

   public MissingSessionException(String message) {
      super(message);
   }

   public MissingSessionException(String message, Throwable cause) {
      super(message, cause);
   }

   public MissingSessionException(Throwable cause) {
      super(cause);
   }
}
