package com.sk89q.worldedit.util.command;

import com.google.common.base.Joiner;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleDispatcher implements Dispatcher {
   private final Map<String, CommandMapping> commands = new HashMap<>();
   private final SimpleDescription description = new SimpleDescription();

   public SimpleDispatcher() {
      this.description.getParameters().add(new SimpleParameter("subcommand"));
      SimpleParameter extraArgs = new SimpleParameter("...");
      extraArgs.setOptional(true);
      this.description.getParameters().add(extraArgs);
   }

   @Override
   public void registerCommand(CommandCallable callable, String... alias) {
      CommandMapping mapping = new SimpleCommandMapping(callable, alias);

      for (String a : alias) {
         String lower = a.toLowerCase();
         if (this.commands.containsKey(lower)) {
            throw new IllegalArgumentException("Replacing commands is currently undefined behavior");
         }
      }

      for (String ax : alias) {
         String lower = ax.toLowerCase();
         this.commands.put(lower, mapping);
      }
   }

   @Override
   public Set<CommandMapping> getCommands() {
      return Collections.unmodifiableSet(new HashSet<>(this.commands.values()));
   }

   public Set<String> getAliases() {
      return Collections.unmodifiableSet(this.commands.keySet());
   }

   public Set<String> getPrimaryAliases() {
      Set<String> aliases = new HashSet<>();

      for (CommandMapping mapping : this.getCommands()) {
         aliases.add(mapping.getPrimaryAlias());
      }

      return Collections.unmodifiableSet(aliases);
   }

   @Override
   public boolean contains(String alias) {
      return this.commands.containsKey(alias.toLowerCase());
   }

   @Override
   public CommandMapping get(String alias) {
      return this.commands.get(alias.toLowerCase());
   }

   @Override
   public Object call(String arguments, CommandLocals locals, String[] parentCommands) throws CommandException {
      if (!this.testPermission(locals)) {
         throw new CommandPermissionsException();
      } else {
         String[] split = CommandContext.split(arguments);
         Set<String> aliases = this.getPrimaryAliases();
         if (aliases.isEmpty()) {
            throw new InvalidUsageException("This command has no sub-commands.", this);
         } else {
            if (split.length > 0) {
               String subCommand = split[0];
               String subArguments = Joiner.on(" ").join(Arrays.copyOfRange(split, 1, split.length));
               String[] subParents = Arrays.copyOf(parentCommands, parentCommands.length + 1);
               subParents[parentCommands.length] = subCommand;
               CommandMapping mapping = this.get(subCommand);
               if (mapping != null) {
                  try {
                     return mapping.getCallable().call(subArguments, locals, subParents);
                  } catch (CommandException var11) {
                     var11.prependStack(subCommand);
                     throw var11;
                  } catch (Throwable var12) {
                     throw new WrappedCommandException(var12);
                  }
               }
            }

            throw new InvalidUsageException("Please choose a sub-command.", this, true);
         }
      }
   }

   @Override
   public List<String> getSuggestions(String arguments, CommandLocals locals) throws CommandException {
      String[] split = CommandContext.split(arguments);
      if (split.length > 1) {
         String subCommand = split[0];
         CommandMapping mapping = this.get(subCommand);
         String passedArguments = Joiner.on(" ").join(Arrays.copyOfRange(split, 1, split.length));
         return mapping != null ? mapping.getCallable().getSuggestions(passedArguments, locals) : Collections.emptyList();
      } else {
         String prefix = split.length > 0 ? split[0] : "";
         List<String> suggestions = new ArrayList<>();

         for (CommandMapping mapping : this.getCommands()) {
            if (mapping.getCallable().testPermission(locals)) {
               for (String alias : mapping.getAllAliases()) {
                  if (prefix.isEmpty() || alias.startsWith(arguments)) {
                     suggestions.add(mapping.getPrimaryAlias());
                     break;
                  }
               }
            }
         }

         return suggestions;
      }
   }

   public SimpleDescription getDescription() {
      return this.description;
   }

   @Override
   public boolean testPermission(CommandLocals locals) {
      for (CommandMapping mapping : this.getCommands()) {
         if (mapping.getCallable().testPermission(locals)) {
            return true;
         }
      }

      return false;
   }
}
