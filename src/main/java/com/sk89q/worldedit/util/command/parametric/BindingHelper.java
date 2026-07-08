package com.sk89q.worldedit.util.command.parametric;

import com.sk89q.minecraft.util.commands.CommandException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BindingHelper implements Binding {
   private final List<BindingHelper.BoundMethod> bindings;
   private final Type[] types;

   public BindingHelper() {
      List<BindingHelper.BoundMethod> bindings = new ArrayList<>();
      List<Type> types = new ArrayList<>();

      for (Method method : this.getClass().getMethods()) {
         BindingMatch info = method.getAnnotation(BindingMatch.class);
         if (info != null) {
            Class<? extends Annotation> classifier = null;
            if (!info.classifier().equals(Annotation.class)) {
               classifier = info.classifier();
               types.add(classifier);
            }

            for (Type t : info.type()) {
               Type type = null;
               if (!t.equals(Class.class)) {
                  type = t;
                  if (classifier == null) {
                     types.add(t);
                  }
               }

               if (type == null && classifier == null) {
                  throw new RuntimeException("A @BindingMatch needs either a type or classifier set");
               }

               BindingHelper.BoundMethod handler = new BindingHelper.BoundMethod(info, type, classifier, method);
               bindings.add(handler);
            }
         }
      }

      Collections.sort(bindings);
      this.bindings = bindings;
      Type[] typesArray = new Type[types.size()];
      types.toArray(typesArray);
      this.types = typesArray;
   }

   private BindingHelper.BoundMethod match(ParameterData parameter) {
      for (BindingHelper.BoundMethod binding : this.bindings) {
         Annotation classifer = parameter.getClassifier();
         Type type = parameter.getType();
         if (binding.classifier != null) {
            if (classifer != null && classifer.annotationType().equals(binding.classifier) && (binding.type == null || binding.type.equals(type))) {
               return binding;
            }
         } else if (binding.type.equals(type)) {
            return binding;
         }
      }

      throw new RuntimeException("Unknown type");
   }

   @Override
   public Type[] getTypes() {
      return this.types;
   }

   @Override
   public int getConsumedCount(ParameterData parameter) {
      return this.match(parameter).annotation.consumedCount();
   }

   @Override
   public BindingBehavior getBehavior(ParameterData parameter) {
      return this.match(parameter).annotation.behavior();
   }

   @Override
   public Object bind(ParameterData parameter, ArgumentStack scoped, boolean onlyConsume) throws ParameterException, CommandException, InvocationTargetException {
      BindingHelper.BoundMethod binding = this.match(parameter);
      List<Object> args = new ArrayList<>();
      args.add(scoped);
      if (binding.classifier != null) {
         args.add(parameter.getClassifier());
      }

      if (binding.annotation.provideModifiers()) {
         args.add(parameter.getModifiers());
      }

      if (onlyConsume && binding.annotation.behavior() == BindingBehavior.PROVIDES) {
         return null;
      } else {
         Object[] argsArray = new Object[args.size()];
         args.toArray(argsArray);

         try {
            return binding.method.invoke(this, argsArray);
         } catch (IllegalArgumentException var8) {
            throw new RuntimeException(
               "Processing of classifier "
                  + parameter.getClassifier()
                  + " and type "
                  + parameter.getType()
                  + " failed for method\n"
                  + binding.method
                  + "\nbecause the parameters for that method are wrong",
               var8
            );
         } catch (IllegalAccessException var9) {
            throw new RuntimeException(var9);
         } catch (InvocationTargetException var10) {
            if (var10.getCause() instanceof ParameterException) {
               throw (ParameterException)var10.getCause();
            } else if (var10.getCause() instanceof CommandException) {
               throw (CommandException)var10.getCause();
            } else {
               throw var10;
            }
         }
      }
   }

   @Override
   public List<String> getSuggestions(ParameterData parameter, String prefix) {
      return new ArrayList<>();
   }

   private static class BoundMethod implements Comparable<BindingHelper.BoundMethod> {
      private final BindingMatch annotation;
      private final Type type;
      private final Class<? extends Annotation> classifier;
      private final Method method;

      BoundMethod(BindingMatch annotation, Type type, Class<? extends Annotation> classifier, Method method) {
         this.annotation = annotation;
         this.type = type;
         this.classifier = classifier;
         this.method = method;
      }

      public int compareTo(BindingHelper.BoundMethod o) {
         if (this.classifier != null && o.classifier == null) {
            return -1;
         } else if (this.classifier == null && o.classifier != null) {
            return 1;
         } else if (this.classifier == null || o.classifier == null) {
            return 0;
         } else if (this.type != null && o.type == null) {
            return -1;
         } else {
            return this.type == null && o.type != null ? 1 : 0;
         }
      }
   }
}
