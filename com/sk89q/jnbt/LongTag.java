package com.sk89q.jnbt;

public final class LongTag extends Tag {
   private final long value;

   public LongTag(long value) {
      this.value = value;
   }

   public Long getValue() {
      return this.value;
   }

   @Override
   public String toString() {
      return "TAG_Long(" + this.value + ")";
   }
}
