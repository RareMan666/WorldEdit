package com.sk89q.worldedit.util.auth;

import com.sk89q.worldedit.WorldEditException;

public class AuthorizationException extends WorldEditException {
   public AuthorizationException() {
   }

   public AuthorizationException(String message) {
      super(message);
   }

   public AuthorizationException(String message, Throwable cause) {
      super(message, cause);
   }

   public AuthorizationException(Throwable cause) {
      super(cause);
   }
}
