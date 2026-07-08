package com.sk89q.worldedit.util.command;

import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;

public interface Dispatcher extends CommandCallable {
   void registerCommand(CommandCallable var1, String... var2);

   Set<CommandMapping> getCommands();

   Collection<String> getPrimaryAliases();

   Collection<String> getAliases();

   @Nullable
   CommandMapping get(String var1);

   boolean contains(String var1);
}
