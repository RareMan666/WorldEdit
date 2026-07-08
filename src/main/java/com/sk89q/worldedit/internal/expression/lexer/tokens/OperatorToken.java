package com.sk89q.worldedit.internal.expression.lexer.tokens;

public class OperatorToken extends Token {
   public final String operator;

   public OperatorToken(int position, String operator) {
      super(position);
      this.operator = operator;
   }

   @Override
   public char id() {
      return 'o';
   }

   @Override
   public String toString() {
      return "OperatorToken(" + this.operator + ")";
   }
}
