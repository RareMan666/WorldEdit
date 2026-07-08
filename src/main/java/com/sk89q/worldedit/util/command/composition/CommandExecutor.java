package com.sk89q.worldedit.util.command.composition;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.argument.MissingArgumentException;
import java.util.List;

public interface CommandExecutor<T> {
   T call(CommandArgs var1, CommandLocals var2) throws CommandException;

   List<String> getSuggestions(CommandArgs var1, CommandLocals var2) throws MissingArgumentException;

   String getUsage();

   String getDescription();

   boolean testPermission(CommandLocals var1);
}
