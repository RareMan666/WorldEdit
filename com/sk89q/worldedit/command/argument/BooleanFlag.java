package com.sk89q.worldedit.command.argument;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.composition.CommandExecutor;
import java.util.Collections;
import java.util.List;

public class BooleanFlag implements CommandExecutor<Boolean> {
   private final String description;

   public BooleanFlag(String description) {
      this.description = description;
   }

   public Boolean call(CommandArgs args, CommandLocals locals) throws CommandException {
      return true;
   }

   @Override
   public List<String> getSuggestions(CommandArgs args, CommandLocals locals) {
      return Collections.emptyList();
   }

   @Override
   public String getUsage() {
      return "";
   }

   @Override
   public String getDescription() {
      return this.description;
   }

   @Override
   public boolean testPermission(CommandLocals locals) {
      return true;
   }
}
