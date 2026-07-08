package com.sk89q.worldedit.session;

public class TransientSessionException extends Exception {
   public TransientSessionException() {
   }

   public TransientSessionException(String message) {
      super(message);
   }

   public TransientSessionException(String message, Throwable cause) {
      super(message, cause);
   }

   public TransientSessionException(Throwable cause) {
      super(cause);
   }
}
