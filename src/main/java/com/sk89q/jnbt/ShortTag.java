package com.sk89q.jnbt;

public final class ShortTag extends Tag {
   private final short value;

   public ShortTag(short value) {
      this.value = value;
   }

   public Short getValue() {
      return this.value;
   }

   @Override
   public String toString() {
      return "TAG_Short(" + this.value + ")";
   }
}
