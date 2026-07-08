package com.sk89q.jchronic.tags;

import java.util.Calendar;

public class Tag<T> {
   private T _type;
   private Calendar _now;

   public Tag(T type) {
      this._type = type;
   }

   public Calendar getNow() {
      return this._now;
   }

   public void setType(T type) {
      this._type = type;
   }

   public T getType() {
      return this._type;
   }

   public void setStart(Calendar s) {
      this._now = s;
   }
}
