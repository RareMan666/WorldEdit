package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;

public final class Variable extends Node implements LValue {
   public double value;

   public Variable(double value) {
      super(-1);
      this.value = value;
   }

   @Override
   public double getValue() {
      return this.value;
   }

   @Override
   public String toString() {
      return "var";
   }

   @Override
   public char id() {
      return 'v';
   }

   @Override
   public double assign(double value) {
      return this.value = value;
   }

   @Override
   public LValue optimize() throws EvaluationException {
      return this;
   }

   @Override
   public LValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
      return this;
   }
}
