package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;

public interface Binding {
   Type[] getTypes();

   BindingBehavior getBehavior(ParameterData var1);

   int getConsumedCount(ParameterData var1);

   Object bind(ParameterData var1, ArgumentStack var2, boolean var3) throws ParameterException, CommandException, InvocationTargetException;

   List<String> getSuggestions(ParameterData var1, String var2);
}
