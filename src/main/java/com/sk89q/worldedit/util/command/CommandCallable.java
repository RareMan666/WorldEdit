package com.sk89q.worldedit.util.command;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;

public interface CommandCallable extends CommandCompleter {
   Object call(String var1, CommandLocals var2, String[] var3) throws CommandException;

   Description getDescription();

   boolean testPermission(CommandLocals var1);
}
