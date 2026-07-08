package com.thoughtworks.paranamer;

import java.lang.reflect.AccessibleObject;

public class NullParanamer implements Paranamer {
   public static final String __PARANAMER_DATA = "lookupParameterNames java.lang.reflect.AccessibleObject methodOrConstructor \nlookupParameterNames java.lang.reflect.AccessibleObject,boolean methodOrConstructor,throwExceptionIfMissing \n";

   @Override
   public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
      return new String[0];
   }

   @Override
   public String[] lookupParameterNames(AccessibleObject methodOrConstructor, boolean throwExceptionIfMissing) {
      if (throwExceptionIfMissing) {
         throw new ParameterNamesNotFoundException("NullParanamer implementation predictably finds no parameter names");
      } else {
         return new String[0];
      }
   }
}
