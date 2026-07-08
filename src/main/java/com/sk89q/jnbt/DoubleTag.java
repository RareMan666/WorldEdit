package com.sk89q.jnbt;

public final class DoubleTag extends Tag {
   private final double value;

   public DoubleTag(double value) {
      this.value = value;
   }

   public Double getValue() {
      return this.value;
   }

   @Override
   public String toString() {
      return "TAG_Double(" + this.value + ")";
   }
}
