package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.worldedit.util.command.SimpleDescription;
import java.lang.reflect.Method;

public abstract class AbstractInvokeListener implements InvokeListener {
   @Override
   public void updateDescription(Object object, Method method, ParameterData[] parameters, SimpleDescription description) {
   }
}
