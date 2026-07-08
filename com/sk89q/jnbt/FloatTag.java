package com.sk89q.jnbt;

public final class FloatTag extends Tag {
   private final float value;

   public FloatTag(float value) {
      this.value = value;
   }

   public Float getValue() {
      return this.value;
   }

   @Override
   public String toString() {
      return "TAG_Float(" + this.value + ")";
   }
}
