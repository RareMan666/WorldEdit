package com.sk89q.worldedit.util.command;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import java.util.Collections;
import java.util.List;

public class NullCompleter implements CommandCompleter {
   @Override
   public List<String> getSuggestions(String arguments, CommandLocals locals) throws CommandException {
      return Collections.emptyList();
   }
}
