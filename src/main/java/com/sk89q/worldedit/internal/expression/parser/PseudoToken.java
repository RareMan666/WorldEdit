package com.sk89q.worldedit.internal.expression.parser;

import com.sk89q.worldedit.internal.expression.Identifiable;

public abstract class PseudoToken implements Identifiable {
   private final int position;

   public PseudoToken(int position) {
      this.position = position;
   }

   @Override
   public abstract char id();

   @Override
   public int getPosition() {
      return this.position;
   }
}
