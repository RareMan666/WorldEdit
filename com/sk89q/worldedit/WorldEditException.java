package com.sk89q.worldedit;

public abstract class WorldEditException extends Exception {
   protected WorldEditException() {
   }

   protected WorldEditException(String message) {
      super(message);
   }

   protected WorldEditException(String message, Throwable cause) {
      super(message, cause);
   }

   protected WorldEditException(Throwable cause) {
      super(cause);
   }
}
