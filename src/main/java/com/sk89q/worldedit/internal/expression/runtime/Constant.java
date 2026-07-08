package com.sk89q.worldedit.internal.expression.runtime;

public final class Constant extends Node {
   private final double value;

   public Constant(int position, double value) {
      super(position);
      this.value = value;
   }

   @Override
   public double getValue() {
      return this.value;
   }

   @Override
   public String toString() {
      return String.valueOf(this.value);
   }

   @Override
   public char id() {
      return 'c';
   }
}
