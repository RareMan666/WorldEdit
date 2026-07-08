package com.sk89q.worldedit.util.command;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.CommandException;
import javax.annotation.Nullable;

public class InvalidUsageException extends CommandException {
   private final CommandCallable command;
   private final boolean fullHelpSuggested;

   public InvalidUsageException(CommandCallable command) {
      this(null, command);
   }

   public InvalidUsageException(@Nullable String message, CommandCallable command) {
      this(message, command, false);
   }

   public InvalidUsageException(@Nullable String message, CommandCallable command, boolean fullHelpSuggested) {
      super(message);
      Preconditions.checkNotNull(command);
      this.command = command;
      this.fullHelpSuggested = fullHelpSuggested;
   }

   public CommandCallable getCommand() {
      return this.command;
   }

   public String getSimpleUsageString(String prefix) {
      return this.getCommandUsed(prefix, this.command.getDescription().getUsage());
   }

   public boolean isFullHelpSuggested() {
      return this.fullHelpSuggested;
   }
}
