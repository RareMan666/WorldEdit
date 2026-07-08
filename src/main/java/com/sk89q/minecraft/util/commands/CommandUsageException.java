package com.sk89q.minecraft.util.commands;

public class CommandUsageException extends CommandException {
   protected String usage;

   public CommandUsageException(String message, String usage) {
      super(message);
      this.usage = usage;
   }

   public String getUsage() {
      return this.usage;
   }
}
