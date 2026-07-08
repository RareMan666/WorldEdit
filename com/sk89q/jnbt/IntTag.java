package com.sk89q.jnbt;

public final class IntTag extends Tag {
   private final int value;

   public IntTag(int value) {
      this.value = value;
   }

   public Integer getValue() {
      return this.value;
   }

   @Override
   public String toString() {
      return "TAG_Int(" + this.value + ")";
   }
}
