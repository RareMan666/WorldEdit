package com.sk89q.jnbt;

public final class ByteTag extends Tag {
   private final byte value;

   public ByteTag(byte value) {
      this.value = value;
   }

   public Byte getValue() {
      return this.value;
   }

   @Override
   public String toString() {
      return "TAG_Byte(" + this.value + ")";
   }
}
