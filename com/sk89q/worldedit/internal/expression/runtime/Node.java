package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;

public abstract class Node implements RValue {
   private final int position;

   public Node(int position) {
      this.position = position;
   }

   @Override
   public abstract String toString();

   @Override
   public RValue optimize() throws EvaluationException {
      return this;
   }

   @Override
   public final int getPosition() {
      return this.position;
   }

   @Override
   public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
      return this;
   }
}
