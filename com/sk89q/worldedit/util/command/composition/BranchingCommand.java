package com.sk89q.worldedit.util.command.composition;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.argument.ArgumentUtils;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.argument.MissingArgumentException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BranchingCommand<T> implements CommandExecutor<T> {
   private final String name;
   private final Map<String, CommandExecutor<? extends T>> options = Maps.newHashMap();
   private final Set<String> primaryAliases = Sets.newHashSet();

   public BranchingCommand(String name) {
      this.name = name;
   }

   public void putOption(CommandExecutor<? extends T> executor, String primaryAlias, String... aliases) {
      this.options.put(primaryAlias, executor);
      this.primaryAliases.add(primaryAlias);

      for (String alias : aliases) {
         this.options.put(alias, executor);
      }
   }

   @Override
   public T call(CommandArgs args, CommandLocals locals) throws CommandException {
      try {
         String classifier = args.next();
         CommandExecutor<? extends T> executor = this.options.get(classifier.toLowerCase());
         if (executor != null) {
            return (T)executor.call(args, locals);
         } else {
            throw new CommandException(
               "'" + classifier + "' isn't a valid option for '" + this.name + "'. Try one of: " + Joiner.on(", ").join(this.primaryAliases)
            );
         }
      } catch (MissingArgumentException var5) {
         throw new CommandException("Missing value for <" + this.name + "> (try one of " + Joiner.on(" | ").join(this.primaryAliases) + ").");
      }
   }

   @Override
   public List<String> getSuggestions(CommandArgs args, CommandLocals locals) throws MissingArgumentException {
      String classifier = args.next();

      try {
         CommandExecutor<? extends T> executor = this.options.get(classifier.toLowerCase());
         if (executor != null) {
            return executor.getSuggestions(args, locals);
         }
      } catch (MissingArgumentException var5) {
      }

      return ArgumentUtils.getMatchingSuggestions(classifier.isEmpty() ? this.primaryAliases : this.options.keySet(), classifier);
   }

   @Override
   public String getUsage() {
      List<String> optionUsages = Lists.newArrayList();

      for (String alias : this.primaryAliases) {
         CommandExecutor<? extends T> executor = this.options.get(alias);
         String usage = executor.getUsage();
         if (usage.isEmpty()) {
            optionUsages.add(alias);
         } else {
            optionUsages.add(alias + " " + executor.getUsage());
         }
      }

      return "(" + Joiner.on(" | ").join(optionUsages) + ")";
   }

   @Override
   public boolean testPermission(CommandLocals locals) {
      for (CommandExecutor<?> executor : this.options.values()) {
         if (!executor.testPermission(locals)) {
            return false;
         }
      }

      return true;
   }
}
