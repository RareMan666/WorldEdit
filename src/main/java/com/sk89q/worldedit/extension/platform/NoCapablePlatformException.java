package com.sk89q.worldedit.extension.platform;

public class NoCapablePlatformException extends RuntimeException {
   public NoCapablePlatformException() {
   }

   public NoCapablePlatformException(String message) {
      super(message);
   }

   public NoCapablePlatformException(String message, Throwable cause) {
      super(message, cause);
   }

   public NoCapablePlatformException(Throwable cause) {
      super(cause);
   }
}
