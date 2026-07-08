package com.sk89q.worldedit.util.command.parametric;

public class ParameterException extends Exception {
   public ParameterException() {
   }

   public ParameterException(String message) {
      super(message);
   }

   public ParameterException(Throwable cause) {
      super(cause);
   }

   public ParameterException(String message, Throwable cause) {
      super(message, cause);
   }
}
