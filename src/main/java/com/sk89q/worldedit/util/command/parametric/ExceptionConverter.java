package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandException;

public interface ExceptionConverter {
   void convert(Throwable var1) throws CommandException;
}
