package com.sk89q.jnbt;

import com.google.common.base.Preconditions;

public final class StringTag extends Tag {
   private final String value;

   public StringTag(String value) {
      Preconditions.checkNotNull(value);
      this.value = value;
   }

   public String getValue() {
      return this.value;
   }

   @Override
   public String toString() {
      return "TAG_String(" + this.value + ")";
   }
}
