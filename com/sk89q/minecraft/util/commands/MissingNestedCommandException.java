package com.sk89q.minecraft.util.commands;

public class MissingNestedCommandException extends CommandUsageException {
   public MissingNestedCommandException(String message, String usage) {
      super(message, usage);
   }
}
