package com.sk89q.worldedit.internal.expression.lexer.tokens;

public class KeywordToken extends Token {
   public final String value;

   public KeywordToken(int position, String value) {
      super(position);
      this.value = value;
   }

   @Override
   public char id() {
      return 'k';
   }

   @Override
   public String toString() {
      return "KeywordToken(" + this.value + ")";
   }
}
