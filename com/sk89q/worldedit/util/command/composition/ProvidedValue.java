package com.sk89q.worldedit.util.command.composition;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.argument.MissingArgumentException;
import java.util.Collections;
import java.util.List;

public class ProvidedValue<T> implements CommandExecutor<T> {
   private final T value;
   private final String description;

   private ProvidedValue(T value, String description) {
      this.value = value;
      this.description = description;
   }

   @Override
   public T call(CommandArgs args, CommandLocals locals) throws CommandException {
      return this.value;
   }

   @Override
   public List<String> getSuggestions(CommandArgs args, CommandLocals locals) throws MissingArgumentException {
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

   public static <T> ProvidedValue<T> create(T value, String description) {
      return new ProvidedValue<>(value, description);
   }
}
