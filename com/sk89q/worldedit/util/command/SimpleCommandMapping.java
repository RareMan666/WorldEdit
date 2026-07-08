package com.sk89q.worldedit.util.command;

import java.util.Arrays;

public class SimpleCommandMapping implements CommandMapping {
   private final String[] aliases;
   private final CommandCallable callable;

   public SimpleCommandMapping(CommandCallable callable, String... alias) {
      this.aliases = alias;
      this.callable = callable;
   }

   @Override
   public String getPrimaryAlias() {
      return this.aliases[0];
   }

   @Override
   public String[] getAllAliases() {
      return this.aliases;
   }

   @Override
   public CommandCallable getCallable() {
      return this.callable;
   }

   @Override
   public Description getDescription() {
      return this.getCallable().getDescription();
   }

   @Override
   public String toString() {
      return "CommandMapping{aliases=" + Arrays.toString((Object[])this.aliases) + ", callable=" + this.callable + '}';
   }
}
