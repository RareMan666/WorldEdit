package com.thoughtworks.paranamer;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class PositionalParanamer implements Paranamer {
   private final String prefix;
   public static final String __PARANAMER_DATA = "<init> java.lang.String prefix \nlookupParameterNames java.lang.reflect.AccessibleObject methodOrConstructor \nlookupParameterNames java.lang.reflect.AccessibleObject,boolean methodOrCtor,throwExceptionIfMissing \n";

   public PositionalParanamer() {
      this("arg");
   }

   public PositionalParanamer(String prefix) {
      this.prefix = prefix;
   }

   @Override
   public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
      return this.lookupParameterNames(methodOrConstructor, true);
   }

   @Override
   public String[] lookupParameterNames(AccessibleObject methodOrCtor, boolean throwExceptionIfMissing) {
      int count;
      if (methodOrCtor instanceof Method) {
         Method method = (Method)methodOrCtor;
         count = method.getParameterTypes().length;
      } else {
         Constructor<?> constructor = (Constructor<?>)methodOrCtor;
         count = constructor.getParameterTypes().length;
      }

      String[] result = new String[count];

      for (int i = 0; i < result.length; i++) {
         result[i] = this.prefix + i;
      }

      return result;
   }
}
