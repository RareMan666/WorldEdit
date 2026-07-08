package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandContext;

public interface ArgumentStack {
   String next() throws ParameterException;

   Integer nextInt() throws ParameterException;

   Double nextDouble() throws ParameterException;

   Boolean nextBoolean() throws ParameterException;

   String remaining() throws ParameterException;

   void markConsumed();

   CommandContext getContext();
}
