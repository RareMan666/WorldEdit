package com.sk89q.worldedit.internal.expression.lexer.tokens;

import com.sk89q.worldedit.internal.expression.Identifiable;

public abstract class Token implements Identifiable {
   private final int position;

   public Token(int position) {
      this.position = position;
   }

   @Override
   public int getPosition() {
      return this.position;
   }
}
