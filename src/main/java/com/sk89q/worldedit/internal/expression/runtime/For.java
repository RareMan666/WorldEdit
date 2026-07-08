package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;

public class For extends Node {
   RValue init;
   RValue condition;
   RValue increment;
   RValue body;

   public For(int position, RValue init, RValue condition, RValue increment, RValue body) {
      super(position);
      this.init = init;
      this.condition = condition;
      this.increment = increment;
      this.body = body;
   }

   @Override
   public double getValue() throws EvaluationException {
      int iterations = 0;
      double ret = 0.0;
      this.init.getValue();

      for (; this.condition.getValue() > 0.0; this.increment.getValue()) {
         if (iterations > 256) {
            throw new EvaluationException(this.getPosition(), "Loop exceeded 256 iterations.");
         }

         iterations++;

         try {
            ret = this.body.getValue();
         } catch (BreakException var5) {
            if (!var5.doContinue) {
               break;
            }
         }
      }

      return ret;
   }

   @Override
   public char id() {
      return 'F';
   }

   @Override
   public String toString() {
      return "for (" + this.init + "; " + this.condition + "; " + this.increment + ") { " + this.body + " }";
   }

   @Override
   public RValue optimize() throws EvaluationException {
      RValue newCondition = this.condition.optimize();
      return (RValue)(newCondition instanceof Constant && newCondition.getValue() <= 0.0
         ? new Sequence(this.getPosition(), this.init, new Constant(this.getPosition(), 0.0)).optimize()
         : new For(this.getPosition(), this.init.optimize(), newCondition, this.increment.optimize(), this.body.optimize()));
   }

   @Override
   public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
      this.init = this.init.bindVariables(expression, false);
      this.condition = this.condition.bindVariables(expression, false);
      this.increment = this.increment.bindVariables(expression, false);
      this.body = this.body.bindVariables(expression, false);
      return this;
   }
}
