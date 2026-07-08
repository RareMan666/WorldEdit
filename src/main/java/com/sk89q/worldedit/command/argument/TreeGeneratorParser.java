package com.sk89q.worldedit.command.argument;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.function.Contextual;
import com.sk89q.worldedit.function.EditContext;
import com.sk89q.worldedit.function.generator.ForestGenerator;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.util.command.argument.ArgumentUtils;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.argument.MissingArgumentException;
import com.sk89q.worldedit.util.command.composition.CommandExecutor;
import java.util.Arrays;
import java.util.List;

public class TreeGeneratorParser implements CommandExecutor<Contextual<ForestGenerator>> {
   private final String name;

   public TreeGeneratorParser(String name) {
      this.name = name;
   }

   private String getOptionsList() {
      return Joiner.on(" | ").join(Arrays.asList(TreeGenerator.TreeType.values()));
   }

   public Contextual<ForestGenerator> call(CommandArgs args, CommandLocals locals) throws CommandException {
      try {
         String input = args.next();
         TreeGenerator.TreeType type = TreeGenerator.lookup(input);
         if (type != null) {
            return new TreeGeneratorParser.GeneratorFactory(type);
         } else {
            throw new CommandException("Unknown value for <" + this.name + "> (try one of " + this.getOptionsList() + ").");
         }
      } catch (MissingArgumentException var5) {
         throw new CommandException("Missing value for <" + this.name + "> (try one of " + this.getOptionsList() + ").");
      }
   }

   @Override
   public List<String> getSuggestions(CommandArgs args, CommandLocals locals) throws MissingArgumentException {
      String s = args.next();
      return (List<String>)(s.isEmpty()
         ? Lists.newArrayList(TreeGenerator.TreeType.getPrimaryAliases())
         : ArgumentUtils.getMatchingSuggestions(TreeGenerator.TreeType.getAliases(), s));
   }

   @Override
   public String getUsage() {
      return "<" + this.name + ">";
   }

   @Override
   public String getDescription() {
      return "Choose a tree generator";
   }

   @Override
   public boolean testPermission(CommandLocals locals) {
      return true;
   }

   private static final class GeneratorFactory implements Contextual<ForestGenerator> {
      private final TreeGenerator.TreeType type;

      private GeneratorFactory(TreeGenerator.TreeType type) {
         this.type = type;
      }

      public ForestGenerator createFromContext(EditContext input) {
         return new ForestGenerator((EditSession)input.getDestination(), new TreeGenerator(this.type));
      }

      @Override
      public String toString() {
         return "tree of type " + this.type;
      }
   }
}
