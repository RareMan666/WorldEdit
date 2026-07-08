package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;

public class Return extends Node {
   RValue value;

   public Return(int position, RValue value) {
      super(position);
      this.value = value;
   }

   @Override
   public double getValue() throws EvaluationException {
      throw new ReturnException(this.value.getValue());
   }

   @Override
   public char id() {
      return 'r';
   }

   @Override
   public String toString() {
      return "return " + this.value;
   }

   @Override
   public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
      this.value = this.value.bindVariables(expression, false);
      return this;
   }
}
