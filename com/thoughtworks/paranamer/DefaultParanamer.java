package com.thoughtworks.paranamer;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DefaultParanamer implements Paranamer {
   private static final String COMMA = ",";
   private static final String SPACE = " ";
   public static final String __PARANAMER_DATA = "v1.0 \nlookupParameterNames java.lang.AccessibleObject methodOrConstructor \nlookupParameterNames java.lang.AccessibleObject,boolean methodOrCtor,throwExceptionIfMissing \ngetParameterTypeName java.lang.Class cls\n";

   @Override
   public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
      return this.lookupParameterNames(methodOrConstructor, true);
   }

   @Override
   public String[] lookupParameterNames(AccessibleObject methodOrCtor, boolean throwExceptionIfMissing) {
      Class<?>[] types = null;
      Class<?> declaringClass = null;
      String name = null;
      if (methodOrCtor instanceof Method) {
         Method method = (Method)methodOrCtor;
         types = method.getParameterTypes();
         name = method.getName();
         declaringClass = method.getDeclaringClass();
      } else {
         Constructor<?> constructor = (Constructor<?>)methodOrCtor;
         types = constructor.getParameterTypes();
         declaringClass = constructor.getDeclaringClass();
         name = "<init>";
      }

      if (types.length == 0) {
         return EMPTY_NAMES;
      } else {
         String parameterTypeNames = getParameterTypeNamesCSV(types);
         String[] names = getParameterNames(declaringClass, parameterTypeNames, name + " ");
         if (names == null) {
            if (throwExceptionIfMissing) {
               throw new ParameterNamesNotFoundException(
                  "No parameter names found for class '" + declaringClass + "', methodOrCtor " + name + " and parameter types " + parameterTypeNames
               );
            } else {
               return Paranamer.EMPTY_NAMES;
            }
         } else {
            return names;
         }
      }
   }

   private static String[] getParameterNames(Class<?> declaringClass, String parameterTypes, String prefix) {
      String data = getParameterListResource(declaringClass);
      String line = findFirstMatchingLine(data, prefix + parameterTypes + " ");
      String[] parts = line.split(" ");
      if (parts.length == 3 && parts[1].equals(parameterTypes)) {
         String parameterNames = parts[2];
         return parameterNames.split(",");
      } else {
         return Paranamer.EMPTY_NAMES;
      }
   }

   static String getParameterTypeNamesCSV(Class<?>[] parameterTypes) {
      StringBuffer sb = new StringBuffer();

      for (int i = 0; i < parameterTypes.length; i++) {
         sb.append(getParameterTypeName(parameterTypes[i]));
         if (i < parameterTypes.length - 1) {
            sb.append(",");
         }
      }

      return sb.toString();
   }

   private static String getParameterListResource(Class<?> declaringClass) {
      try {
         Field field = declaringClass.getDeclaredField("__PARANAMER_DATA");
         return Modifier.isStatic(field.getModifiers()) && field.getType().equals(String.class) ? (String)field.get(null) : null;
      } catch (NoSuchFieldException var2) {
         return null;
      } catch (IllegalAccessException var3) {
         return null;
      }
   }

   private static String findFirstMatchingLine(String data, String prefix) {
      if (data == null) {
         return "";
      } else {
         int ix = data.indexOf(prefix);
         if (ix >= 0) {
            int iy = data.indexOf("\n", ix);
            if (iy > 0) {
               return data.substring(ix, iy);
            }
         }

         return "";
      }
   }

   private static String getParameterTypeName(Class<?> cls) {
      String parameterTypeNameName = cls.getName();
      parameterTypeNameName = parameterTypeNameName.replace("[J", "[Xlong")
         .replace("[I", "[Xint")
         .replace("[Z", "[Xboolean")
         .replace("[S", "[Xshort")
         .replace("[F", "[Xfloat")
         .replace("[D", "[Xdouble")
         .replace("[B", "[Xbyte")
         .replace("[C", "[Xchar");
      int arrayNestingDepth = 0;

      for (int ix = parameterTypeNameName.indexOf("["); ix > -1; ix = parameterTypeNameName.indexOf("[")) {
         arrayNestingDepth++;
         parameterTypeNameName = parameterTypeNameName.replaceFirst("(\\[\\w)|(\\[)", "");
      }

      parameterTypeNameName = parameterTypeNameName.replaceFirst(";", "");

      for (int k = 0; k < arrayNestingDepth; k++) {
         parameterTypeNameName = parameterTypeNameName + "[]";
      }

      return parameterTypeNameName;
   }
}
