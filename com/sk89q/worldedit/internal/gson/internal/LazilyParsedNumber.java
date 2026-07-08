package com.sk89q.worldedit.internal.gson.internal;

import java.io.ObjectStreamException;
import java.math.BigDecimal;

public final class LazilyParsedNumber extends Number {
   private final String value;

   public LazilyParsedNumber(String value) {
      this.value = value;
   }

   @Override
   public int intValue() {
      try {
         return Integer.parseInt(this.value);
      } catch (NumberFormatException var4) {
         try {
            return (int)Long.parseLong(this.value);
         } catch (NumberFormatException var3) {
            return new BigDecimal(this.value).intValue();
         }
      }
   }

   @Override
   public long longValue() {
      try {
         return Long.parseLong(this.value);
      } catch (NumberFormatException var2) {
         return new BigDecimal(this.value).longValue();
      }
   }

   @Override
   public float floatValue() {
      return Float.parseFloat(this.value);
   }

   @Override
   public double doubleValue() {
      return Double.parseDouble(this.value);
   }

   @Override
   public String toString() {
      return this.value;
   }

   private Object writeReplace() throws ObjectStreamException {
      return new BigDecimal(this.value);
   }
}
