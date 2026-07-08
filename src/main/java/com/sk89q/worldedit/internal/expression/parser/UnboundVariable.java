package com.sk89q.worldedit.internal.expression.parser;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;
import com.sk89q.worldedit.internal.expression.runtime.LValue;
import com.sk89q.worldedit.internal.expression.runtime.RValue;

public class UnboundVariable extends PseudoToken implements LValue {
   public final String name;

   public UnboundVariable(int position, String name) {
      super(position);
      this.name = name;
   }

   @Override
   public char id() {
      return 'V';
   }

   @Override
   public String toString() {
      return "UnboundVariable(" + this.name + ")";
   }

   @Override
   public double getValue() throws EvaluationException {
      throw new EvaluationException(this.getPosition(), "Tried to evaluate unbound variable!");
   }

   @Override
   public LValue optimize() throws EvaluationException {
      throw new EvaluationException(this.getPosition(), "Tried to optimize unbound variable!");
   }

   @Override
   public double assign(double value) throws EvaluationException {
      throw new EvaluationException(this.getPosition(), "Tried to assign unbound variable!");
   }

   public RValue bind(Expression expression, boolean isLValue) throws ParserException {
      RValue variable = expression.getVariable(this.name, isLValue);
      if (variable == null) {
         throw new ParserException(this.getPosition(), "Variable '" + this.name + "' not found");
      } else {
         return variable;
      }
   }

   @Override
   public LValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
      RValue variable = expression.getVariable(this.name, preferLValue);
      if (variable == null) {
         throw new ParserException(this.getPosition(), "Variable '" + this.name + "' not found");
      } else {
         return (LValue)variable;
      }
   }
}
