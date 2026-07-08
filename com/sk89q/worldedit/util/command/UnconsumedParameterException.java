package com.sk89q.worldedit.util.command;

import com.sk89q.worldedit.util.command.parametric.ParameterException;

public class UnconsumedParameterException extends ParameterException {
   private String unconsumed;

   public UnconsumedParameterException(String unconsumed) {
      this.unconsumed = unconsumed;
   }

   public String getUnconsumed() {
      return this.unconsumed;
   }
}
