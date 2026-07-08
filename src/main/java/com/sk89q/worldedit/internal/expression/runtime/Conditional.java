package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;

public class Conditional extends Node {
   private RValue condition;
   private RValue truePart;
   private RValue falsePart;

   public Conditional(int position, RValue condition, RValue truePart, RValue falsePart) {
      super(position);
      this.condition = condition;
      this.truePart = truePart;
      this.falsePart = falsePart;
   }

   @Override
   public double getValue() throws EvaluationException {
      if (this.condition.getValue() > 0.0) {
         return this.truePart.getValue();
      } else {
         return this.falsePart == null ? 0.0 : this.falsePart.getValue();
      }
   }

   @Override
   public char id() {
      return 'I';
   }

   @Override
   public String toString() {
      if (this.falsePart == null) {
         return "if (" + this.condition + ") { " + this.truePart + " }";
      } else {
         return !(this.truePart instanceof Sequence) && !(this.falsePart instanceof Sequence)
            ? "(" + this.condition + ") ? (" + this.truePart + ") : (" + this.falsePart + ")"
            : "if (" + this.condition + ") { " + this.truePart + " } else { " + this.falsePart + " }";
      }
   }

   @Override
   public RValue optimize() throws EvaluationException {
      RValue newCondition = this.condition.optimize();
      if (newCondition instanceof Constant) {
         if (newCondition.getValue() > 0.0) {
            return this.truePart.optimize();
         } else {
            return (RValue)(this.falsePart == null ? new Constant(this.getPosition(), 0.0) : this.falsePart.optimize());
         }
      } else {
         return new Conditional(this.getPosition(), newCondition, this.truePart.optimize(), this.falsePart == null ? null : this.falsePart.optimize());
      }
   }

   @Override
   public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
      this.condition = this.condition.bindVariables(expression, false);
      this.truePart = this.truePart.bindVariables(expression, false);
      if (this.falsePart != null) {
         this.falsePart = this.falsePart.bindVariables(expression, false);
      }

      return this;
   }
}
