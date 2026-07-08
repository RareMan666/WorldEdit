package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ExceptionConverterHelper implements ExceptionConverter {
   private final List<ExceptionConverterHelper.ExceptionHandler> handlers;

   public ExceptionConverterHelper() {
      List<ExceptionConverterHelper.ExceptionHandler> handlers = new ArrayList<>();

      for (Method method : this.getClass().getMethods()) {
         if (method.getAnnotation(ExceptionMatch.class) != null) {
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length == 1) {
               Class<?> cls = parameters[0];
               if (Throwable.class.isAssignableFrom(cls)) {
                  handlers.add(new ExceptionConverterHelper.ExceptionHandler((Class<? extends Throwable>) cls, method));
               }
            }
         }
      }

      Collections.sort(handlers);
      this.handlers = handlers;
   }

   @Override
   public void convert(Throwable t) throws CommandException {
      Class<?> throwableClass = t.getClass();

      for (ExceptionConverterHelper.ExceptionHandler handler : this.handlers) {
         if (handler.cls.isAssignableFrom(throwableClass)) {
            try {
               handler.method.invoke(this, t);
            } catch (InvocationTargetException var6) {
               if (var6.getCause() instanceof CommandException) {
                  throw (CommandException)var6.getCause();
               }

               throw new WrappedCommandException(var6);
            } catch (IllegalArgumentException var7) {
               throw new WrappedCommandException(var7);
            } catch (IllegalAccessException var8) {
               throw new WrappedCommandException(var8);
            }
         }
      }
   }

   private static class ExceptionHandler implements Comparable<ExceptionConverterHelper.ExceptionHandler> {
      final Class<? extends Throwable> cls;
      final Method method;

      private ExceptionHandler(Class<? extends Throwable> cls, Method method) {
         this.cls = cls;
         this.method = method;
      }

      public int compareTo(ExceptionConverterHelper.ExceptionHandler o) {
         if (this.cls.equals(o.cls)) {
            return 0;
         } else if (this.cls.isAssignableFrom(o.cls)) {
            return 1;
         } else {
            return o.cls.isAssignableFrom(this.cls) ? -1 : this.cls.getCanonicalName().compareTo(o.cls.getCanonicalName());
         }
      }
   }
}
