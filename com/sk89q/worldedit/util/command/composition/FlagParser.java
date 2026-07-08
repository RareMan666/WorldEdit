package com.sk89q.worldedit.util.command.composition;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.argument.MissingArgumentException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

public class FlagParser implements CommandExecutor<FlagParser.FlagData> {
   private final Map<Character, CommandExecutor<?>> flags = Maps.newHashMap();

   public <T> FlagParser.Flag<T> registerFlag(char flag, CommandExecutor<T> executor) {
      FlagParser.Flag<T> ret = new FlagParser.Flag<>(flag);
      this.flags.put(flag, executor);
      return ret;
   }

   public FlagParser.FlagData call(CommandArgs args, CommandLocals locals) throws CommandException {
      Map<Character, Object> values = Maps.newHashMap();

      try {
         while (true) {
            String next = args.peek();
            if (next.equals("--")) {
               args.next();
               break;
            }

            if (next.length() <= 0 || next.charAt(0) != '-') {
               break;
            }

            args.next();
            if (next.length() == 1) {
               throw new CommandException("- must be followed by a flag (like -a), otherwise use -- before the - (i.e. /cmd -- - is a dash).");
            }

            for (int i = 1; i < next.length(); i++) {
               char flag = next.charAt(i);
               CommandExecutor<?> executor = this.flags.get(flag);
               if (executor == null) {
                  throw new CommandException(
                     "Unknown flag: -"
                        + flag
                        + " (try one of -"
                        + Joiner.on("").join(this.flags.keySet())
                        + " or put -- to skip flag parsing, i.e. /cmd -- -this begins with a dash)."
                  );
               }

               values.put(flag, executor.call(args, locals));
            }
         }
      } catch (MissingArgumentException var8) {
      }

      return new FlagParser.FlagData(values);
   }

   @Override
   public List<String> getSuggestions(CommandArgs args, CommandLocals locals) throws MissingArgumentException {
      List<String> suggestions = Collections.emptyList();

      while (true) {
         String next = args.peek();
         if (next.equals("--")) {
            args.next();
            return suggestions;
         }

         if (next.length() <= 0 || next.charAt(0) != '-') {
            return suggestions;
         }

         args.next();
         if (!args.hasNext()) {
            List<String> flagSuggestions = Lists.newArrayList();

            for (Character flag : this.flags.keySet()) {
               if (next.indexOf(flag) < 1) {
                  flagSuggestions.add(next + flag);
               }
            }

            return flagSuggestions;
         }

         for (int i = 1; i < next.length(); i++) {
            char flagx = next.charAt(i);
            CommandExecutor<?> executor = this.flags.get(flagx);
            if (executor == null) {
               return suggestions;
            }

            suggestions = executor.getSuggestions(args, locals);
         }
      }
   }

   @Override
   public String getUsage() {
      List<String> options = Lists.newArrayList();

      for (Entry<Character, CommandExecutor<?>> entry : this.flags.entrySet()) {
         String usage = entry.getValue().getUsage();
         options.add("[-" + entry.getKey() + (!usage.isEmpty() ? " " + usage : "") + "]");
      }

      return Joiner.on(" ").join(options);
   }

   @Override
   public String getDescription() {
      return "Read flags";
   }

   @Override
   public boolean testPermission(CommandLocals locals) {
      for (CommandExecutor<?> executor : this.flags.values()) {
         if (!executor.testPermission(locals)) {
            return false;
         }
      }

      return true;
   }

   public static final class Flag<T> {
      private final char flag;

      private Flag(char flag) {
         this.flag = flag;
      }

      @Nullable
      public T get(FlagParser.FlagData data) {
         return (T)data.get(this.flag);
      }

      public T get(FlagParser.FlagData data, T fallback) {
         T value = this.get(data);
         return value == null ? fallback : value;
      }
   }

   public static class FlagData {
      private final Map<Character, Object> data;

      private FlagData(Map<Character, Object> data) {
         this.data = data;
      }

      public int size() {
         return this.data.size();
      }

      public boolean isEmpty() {
         return this.data.isEmpty();
      }

      public Object get(char key) {
         return this.data.get(key);
      }

      public boolean containsKey(char key) {
         return this.data.containsKey(key);
      }
   }
}
