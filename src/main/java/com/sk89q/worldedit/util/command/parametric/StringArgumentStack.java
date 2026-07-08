package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.util.StringUtil;
import com.sk89q.worldedit.util.command.MissingParameterException;

public class StringArgumentStack implements ArgumentStack {
   private final boolean nonNullBoolean;
   private final CommandContext context;
   private final String[] arguments;
   private int index = 0;

   public StringArgumentStack(CommandContext context, String[] arguments, boolean nonNullBoolean) {
      this.context = context;
      this.arguments = arguments;
      this.nonNullBoolean = nonNullBoolean;
   }

   public StringArgumentStack(CommandContext context, String arguments, boolean nonNullBoolean) {
      this.context = context;
      this.arguments = CommandContext.split(arguments);
      this.nonNullBoolean = nonNullBoolean;
   }

   @Override
   public String next() throws ParameterException {
      try {
         return this.arguments[this.index++];
      } catch (ArrayIndexOutOfBoundsException var2) {
         throw new MissingParameterException();
      }
   }

   @Override
   public Integer nextInt() throws ParameterException {
      try {
         return Integer.parseInt(this.next());
      } catch (NumberFormatException var2) {
         throw new ParameterException("Expected a number, got '" + this.context.getString(this.index - 1) + "'");
      }
   }

   @Override
   public Double nextDouble() throws ParameterException {
      try {
         return Double.parseDouble(this.next());
      } catch (NumberFormatException var2) {
         throw new ParameterException("Expected a number, got '" + this.context.getString(this.index - 1) + "'");
      }
   }

   @Override
   public Boolean nextBoolean() throws ParameterException {
      try {
         return this.next().equalsIgnoreCase("true");
      } catch (IndexOutOfBoundsException var2) {
         if (this.nonNullBoolean) {
            return false;
         } else {
            throw new MissingParameterException();
         }
      }
   }

   @Override
   public String remaining() throws ParameterException {
      try {
         String value = StringUtil.joinString(this.arguments, " ", this.index);
         this.markConsumed();
         return value;
      } catch (IndexOutOfBoundsException var2) {
         throw new MissingParameterException();
      }
   }

   @Override
   public void markConsumed() {
      this.index = this.arguments.length;
   }

   @Override
   public CommandContext getContext() {
      return this.context;
   }
}
