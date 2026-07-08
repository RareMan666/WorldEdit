package com.sk89q.worldedit.util.command;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import java.util.List;

public interface CommandCompleter {
   List<String> getSuggestions(String var1, CommandLocals var2) throws CommandException;
}
