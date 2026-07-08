package com.thoughtworks.paranamer;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.inject.Named;

public class AnnotationParanamer implements Paranamer {
   public static final String __PARANAMER_DATA = "v1.0 \nlookupParameterNames java.lang.AccessibleObject methodOrConstructor \nlookupParameterNames java.lang.AccessibleObject,boolean methodOrCtor,throwExceptionIfMissing \n";
   private final Paranamer fallback;

   public AnnotationParanamer() {
      this(new NullParanamer());
   }

   public AnnotationParanamer(Paranamer fallback) {
      this.fallback = fallback;
   }

   @Override
   public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
      return this.lookupParameterNames(methodOrConstructor, true);
   }

   @Override
   public String[] lookupParameterNames(AccessibleObject methodOrCtor, boolean throwExceptionIfMissing) {
      Class<?>[] types = null;
      Class<?> declaringClass = null;
      String name = null;
      Annotation[][] anns = (Annotation[][])null;
      if (methodOrCtor instanceof Method) {
         Method method = (Method)methodOrCtor;
         types = method.getParameterTypes();
         name = method.getName();
         declaringClass = method.getDeclaringClass();
         anns = method.getParameterAnnotations();
      } else {
         Constructor<?> constructor = (Constructor<?>)methodOrCtor;
         types = constructor.getParameterTypes();
         declaringClass = constructor.getDeclaringClass();
         name = "<init>";
         anns = constructor.getParameterAnnotations();
      }

      if (types.length == 0) {
         return EMPTY_NAMES;
      } else {
         String[] names = new String[types.length];
         boolean allDone = true;

         for (int i = 0; i < names.length; i++) {
            for (int j = 0; j < anns[i].length; j++) {
               Annotation ann = anns[i][j];
               if (this.isNamed(ann)) {
                  names[i] = this.getNamedValue(ann);
                  break;
               }
            }

            if (names[i] == null) {
               allDone = false;
            }
         }

         if (!allDone) {
            allDone = true;
            String[] altNames = this.fallback.lookupParameterNames(methodOrCtor, false);
            if (altNames.length > 0) {
               for (int i = 0; i < names.length; i++) {
                  if (names[i] == null) {
                     if (altNames[i] != null) {
                        names[i] = altNames[i];
                     } else {
                        allDone = false;
                     }
                  }
               }
            } else {
               allDone = false;
            }
         }

         if (allDone) {
            return names;
         } else if (throwExceptionIfMissing) {
            throw new ParameterNamesNotFoundException(
               "One or more @Named annotations missing for class '"
                  + declaringClass.getName()
                  + "', methodOrCtor "
                  + name
                  + " and parameter types "
                  + DefaultParanamer.getParameterTypeNamesCSV(types)
            );
         } else {
            return Paranamer.EMPTY_NAMES;
         }
      }
   }

   protected String getNamedValue(Annotation ann) {
      return "javax.inject.Named".equals(ann.annotationType().getName()) ? AnnotationParanamer.Jsr330Helper.getNamedValue(ann) : null;
   }

   protected boolean isNamed(Annotation ann) {
      return "javax.inject.Named".equals(ann.annotationType().getName()) ? AnnotationParanamer.Jsr330Helper.isNamed(ann) : false;
   }

   public static class Jsr330Helper {
      public static final String __PARANAMER_DATA = "";

      private static boolean isNamed(Annotation ann) {
         return ann instanceof Named;
      }

      private static String getNamedValue(Annotation ann) {
         return ((Named)ann).value();
      }
   }
}
