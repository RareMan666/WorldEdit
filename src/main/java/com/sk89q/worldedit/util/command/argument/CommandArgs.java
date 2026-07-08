package com.sk89q.worldedit.util.command.argument;

import com.google.common.collect.Lists;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import java.util.Collections;
import java.util.List;

public class CommandArgs {
   private final List<String> arguments;
   private int position = 0;

   public CommandArgs(List<String> arguments) {
      this.arguments = arguments;
   }

   public CommandArgs(CommandArgs args) {
      this(Lists.newArrayList(args.arguments));
   }

   public boolean hasNext() {
      return this.position < this.arguments.size();
   }

   public String next() throws MissingArgumentException {
      try {
         return this.arguments.get(this.position++);
      } catch (IndexOutOfBoundsException var2) {
         throw new MissingArgumentException("Too few arguments specified.");
      }
   }

   public String uncheckedNext() {
      return this.hasNext() ? this.arguments.get(this.position) : null;
   }

   public String peek() throws MissingArgumentException {
      try {
         return this.arguments.get(this.position);
      } catch (IndexOutOfBoundsException var2) {
         throw new MissingArgumentException("Too few arguments specified.");
      }
   }

   public String uncheckedPeek() {
      return this.hasNext() ? this.arguments.get(this.position) : null;
   }

   public String remaining() throws MissingArgumentException {
      if (this.hasNext()) {
         StringBuilder builder = new StringBuilder();

         for (boolean first = true; this.hasNext(); first = false) {
            if (!first) {
               builder.append(" ");
            }

            builder.append(this.next());
         }

         return builder.toString();
      } else {
         throw new MissingArgumentException("Too few arguments specified.");
      }
   }

   public String peekRemaining() throws MissingArgumentException {
      if (this.hasNext()) {
         StringBuilder builder = new StringBuilder();

         for (boolean first = true; this.hasNext(); first = false) {
            if (!first) {
               builder.append(" ");
            }

            builder.append(this.next());
         }

         return builder.toString();
      } else {
         throw new MissingArgumentException();
      }
   }

   public int position() {
      return this.position;
   }

   public int size() {
      return this.arguments.size();
   }

   public void markConsumed() {
      this.position = this.arguments.size();
   }

   public void requireAllConsumed() throws UnusedArgumentsException {
      if (this.hasNext()) {
         StringBuilder builder = new StringBuilder();

         try {
            builder.append(this.peekRemaining());
         } catch (MissingArgumentException var3) {
            throw new RuntimeException("This should not have happened", var3);
         }

         throw new UnusedArgumentsException("There were unused arguments: " + builder);
      }
   }

   public static class Parser {
      private boolean usingHangingArguments = false;

      public boolean isUsingHangingArguments() {
         return this.usingHangingArguments;
      }

      public CommandArgs.Parser setUsingHangingArguments(boolean usingHangingArguments) {
         this.usingHangingArguments = usingHangingArguments;
         return this;
      }

      public CommandArgs parse(String arguments) throws CommandException {
         CommandContext context = new CommandContext(CommandContext.split("_ " + arguments), Collections.emptySet(), false, null, false);
         List<String> args = Lists.newArrayList();

         for (int i = 0; i < context.argsLength(); i++) {
            args.add(context.getString(i));
         }

         if (this.isUsingHangingArguments() && (arguments.isEmpty() || arguments.endsWith(" "))) {
            args.add("");
         }

         return new CommandArgs(args);
      }
   }
}
