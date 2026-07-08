package com.sk89q.minecraft.util.commands;

public class WrappedCommandException extends CommandException {
   public WrappedCommandException(Throwable t) {
      super(t);
   }
}
