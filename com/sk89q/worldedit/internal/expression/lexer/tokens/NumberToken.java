package com.sk89q.worldedit.internal.expression.lexer.tokens;

public class NumberToken extends Token {
   public final double value;

   public NumberToken(int position, double value) {
      super(position);
      this.value = value;
   }

   @Override
   public char id() {
      return '0';
   }

   @Override
   public String toString() {
      return "NumberToken(" + this.value + ")";
   }
}
