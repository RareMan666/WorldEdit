package com.sk89q.worldedit.internal.expression.lexer.tokens;

public class IdentifierToken extends Token {
   public final String value;

   public IdentifierToken(int position, String value) {
      super(position);
      this.value = value;
   }

   @Override
   public char id() {
      return 'i';
   }

   @Override
   public String toString() {
      return "IdentifierToken(" + this.value + ")";
   }
}
