package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.util.command.MissingParameterException;
import com.sk89q.worldedit.util.command.SimpleDescription;
import com.sk89q.worldedit.util.command.UnconsumedParameterException;
import java.lang.reflect.Method;

public class LegacyCommandsHandler extends AbstractInvokeListener implements InvokeHandler {
   @Override
   public InvokeHandler createInvokeHandler() {
      return this;
   }

   @Override
   public void preProcess(Object object, Method method, ParameterData[] parameters, CommandContext context) throws CommandException, ParameterException {
   }

   @Override
   public void preInvoke(Object object, Method method, ParameterData[] parameters, Object[] args, CommandContext context) throws ParameterException {
      Command annotation = method.getAnnotation(Command.class);
      if (annotation != null) {
         if (context.argsLength() < annotation.min()) {
            throw new MissingParameterException();
         }

         if (annotation.max() != -1 && context.argsLength() > annotation.max()) {
            throw new UnconsumedParameterException(context.getRemainingString(annotation.max()));
         }
      }
   }

   @Override
   public void postInvoke(Object object, Method method, ParameterData[] parameters, Object[] args, CommandContext context) {
   }

   @Override
   public void updateDescription(Object object, Method method, ParameterData[] parameters, SimpleDescription description) {
      Command annotation = method.getAnnotation(Command.class);
      if (annotation != null && annotation.usage().isEmpty() && (annotation.min() > 0 || annotation.max() > 0)) {
         boolean hasUserParameters = false;

         for (ParameterData parameter : parameters) {
            if (parameter.getBinding().getBehavior(parameter) != BindingBehavior.PROVIDES) {
               hasUserParameters = true;
               break;
            }
         }

         if (!hasUserParameters) {
            description.overrideUsage("(unknown usage information)");
         }
      }
   }
}
