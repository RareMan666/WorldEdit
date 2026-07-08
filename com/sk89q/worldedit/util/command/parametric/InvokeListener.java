package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.worldedit.util.command.SimpleDescription;
import java.lang.reflect.Method;

public interface InvokeListener {
   InvokeHandler createInvokeHandler();

   void updateDescription(Object var1, Method var2, ParameterData[] var3, SimpleDescription var4);
}
