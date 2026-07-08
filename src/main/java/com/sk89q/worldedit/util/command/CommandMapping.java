package com.sk89q.worldedit.util.command;

public interface CommandMapping {
   String getPrimaryAlias();

   String[] getAllAliases();

   CommandCallable getCallable();

   Description getDescription();
}
