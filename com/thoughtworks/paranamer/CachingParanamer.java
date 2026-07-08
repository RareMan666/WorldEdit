package com.thoughtworks.paranamer;

import java.lang.reflect.AccessibleObject;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class CachingParanamer implements Paranamer {
   public static final String __PARANAMER_DATA = "v1.0 \ncom.thoughtworks.paranamer.CachingParanamer <init> com.thoughtworks.paranamer.Paranamer delegate \ncom.thoughtworks.paranamer.CachingParanamer lookupParameterNames java.lang.AccessibleObject methodOrConstructor \ncom.thoughtworks.paranamer.CachingParanamer lookupParameterNames java.lang.AccessibleObject, boolean methodOrCtor,throwExceptionIfMissing \n";
   private final Paranamer delegate;
   private final Map<AccessibleObject, String[]> methodCache = Collections.synchronizedMap(new WeakHashMap<>());

   public CachingParanamer() {
      this(new DefaultParanamer());
   }

   public CachingParanamer(Paranamer delegate) {
      this.delegate = delegate;
   }

   @Override
   public String[] lookupParameterNames(AccessibleObject methodOrConstructor) {
      return this.lookupParameterNames(methodOrConstructor, true);
   }

   @Override
   public String[] lookupParameterNames(AccessibleObject methodOrCtor, boolean throwExceptionIfMissing) {
      String[] names = this.methodCache.get(methodOrCtor);
      if (names == null) {
         names = this.delegate.lookupParameterNames(methodOrCtor, throwExceptionIfMissing);
         this.methodCache.put(methodOrCtor, names);
      }

      return names;
   }
}
