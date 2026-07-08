package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;

public class SimpleFor extends Node {
   LValue counter;
   RValue first;
   RValue last;
   RValue body;

   public SimpleFor(int position, LValue counter, RValue first, RValue last, RValue body) {
      super(position);
      this.counter = counter;
      this.first = first;
      this.last = last;
      this.body = body;
   }

   @Override
   public double getValue() throws EvaluationException {
      int iterations = 0;
      double ret = 0.0;
      double firstValue = this.first.getValue();
      double lastValue = this.last.getValue();

      for (double i = firstValue; i <= lastValue; i++) {
         if (iterations > 256) {
            throw new EvaluationException(this.getPosition(), "Loop exceeded 256 iterations.");
         }

         iterations++;

         try {
            this.counter.assign(i);
            ret = this.body.getValue();
         } catch (BreakException var11) {
            if (!var11.doContinue) {
               break;
            }
         }
      }

      return ret;
   }

   @Override
   public char id() {
      return 'S';
   }

   @Override
   public String toString() {
      return "for (" + this.counter + " = " + this.first + ", " + this.last + ") { " + this.body + " }";
   }

   @Override
   public RValue optimize() throws EvaluationException {
      return new SimpleFor(this.getPosition(), this.counter.optimize(), this.first.optimize(), this.last.optimize(), this.body.optimize());
   }

   @Override
   public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
      this.counter = this.counter.bindVariables(expression, true);
      this.first = this.first.bindVariables(expression, false);
      this.last = this.last.bindVariables(expression, false);
      this.body = this.body.bindVariables(expression, false);
      return this;
   }
}
