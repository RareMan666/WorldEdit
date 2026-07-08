package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import java.lang.reflect.Method;

public interface InvokeHandler {
   void preProcess(Object var1, Method var2, ParameterData[] var3, CommandContext var4) throws CommandException, ParameterException;

   void preInvoke(Object var1, Method var2, ParameterData[] var3, Object[] var4, CommandContext var5) throws CommandException, ParameterException;

   void postInvoke(Object var1, Method var2, ParameterData[] var3, Object[] var4, CommandContext var5) throws CommandException, ParameterException;
}
