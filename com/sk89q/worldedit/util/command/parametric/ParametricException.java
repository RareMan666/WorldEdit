package com.sk89q.worldedit.util.command.parametric;

public class ParametricException extends RuntimeException {
   public ParametricException() {
   }

   public ParametricException(String message, Throwable cause) {
      super(message, cause);
   }

   public ParametricException(String message) {
      super(message);
   }

   public ParametricException(Throwable cause) {
      super(cause);
   }
}
