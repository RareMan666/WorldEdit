package com.thoughtworks.paranamer;

import java.lang.reflect.AccessibleObject;

public interface Paranamer {
   String[] EMPTY_NAMES = new String[0];
   String __PARANAMER_DATA = "lookupParameterNames java.lang.reflect.AccessibleObject methodOrConstructor \nlookupParameterNames java.lang.reflect.AccessibleObject,boolean methodOrConstructor,throwExceptionIfMissing \n";

   String[] lookupParameterNames(AccessibleObject var1);

   String[] lookupParameterNames(AccessibleObject var1, boolean var2);
}
