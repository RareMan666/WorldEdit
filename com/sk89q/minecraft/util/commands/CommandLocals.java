package com.sk89q.minecraft.util.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandLocals {
   private final Map<Object, Object> locals = new HashMap<>();

   public boolean containsKey(Object key) {
      return this.locals.containsKey(key);
   }

   public boolean containsValue(Object value) {
      return this.locals.containsValue(value);
   }

   public Object get(Object key) {
      return this.locals.get(key);
   }

   public <T> T get(Class<T> key) {
      return (T)this.locals.get(key);
   }

   public Object put(Object key, Object value) {
      return this.locals.put(key, value);
   }
}
