package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.worldedit.util.command.MissingParameterException;

public class ContextArgumentStack implements ArgumentStack {
   private final CommandContext context;
   private int index = 0;
   private int markedIndex = 0;

   public ContextArgumentStack(CommandContext context) {
      this.context = context;
   }

   @Override
   public String next() throws ParameterException {
      try {
         return this.context.getString(this.index++);
      } catch (IndexOutOfBoundsException var2) {
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
         throw new MissingParameterException();
      }
   }

   @Override
   public String remaining() throws ParameterException {
      try {
         String value = this.context.getJoinedStrings(this.index);
         this.index = this.context.argsLength();
         return value;
      } catch (IndexOutOfBoundsException var2) {
         throw new MissingParameterException();
      }
   }

   public String getUnconsumed() {
      return this.index >= this.context.argsLength() ? null : this.context.getJoinedStrings(this.index);
   }

   @Override
   public void markConsumed() {
      this.index = this.context.argsLength();
   }

   public int position() {
      return this.index;
   }

   public void mark() {
      this.markedIndex = this.index;
   }

   public String reset() {
      String value = this.context.getString(this.markedIndex, this.index);
      this.index = this.markedIndex;
      return value;
   }

   public boolean wasConsumed() {
      return this.markedIndex != this.index;
   }

   public String getConsumed() {
      return this.context.getString(this.markedIndex, this.index);
   }

   @Override
   public CommandContext getContext() {
      return this.context;
   }
}
