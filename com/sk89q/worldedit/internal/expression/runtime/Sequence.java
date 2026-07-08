package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sequence extends Node {
   final RValue[] sequence;

   public Sequence(int position, RValue... sequence) {
      super(position);
      this.sequence = sequence;
   }

   @Override
   public char id() {
      return 's';
   }

   @Override
   public double getValue() throws EvaluationException {
      double ret = 0.0;

      for (RValue invokable : this.sequence) {
         ret = invokable.getValue();
      }

      return ret;
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder("seq(");
      boolean first = true;

      for (RValue invokable : this.sequence) {
         if (!first) {
            sb.append(", ");
         }

         sb.append(invokable);
         first = false;
      }

      return sb.append(')').toString();
   }

   @Override
   public RValue optimize() throws EvaluationException {
      List<RValue> newSequence = new ArrayList<>();
      RValue droppedLast = null;

      for (RValue invokable : this.sequence) {
         droppedLast = null;
         invokable = invokable.optimize();
         if (invokable instanceof Sequence) {
            Collections.addAll(newSequence, ((Sequence)invokable).sequence);
         } else if (invokable instanceof Constant) {
            droppedLast = invokable;
         } else {
            newSequence.add(invokable);
         }
      }

      if (droppedLast != null) {
         newSequence.add(droppedLast);
      }

      return (RValue)(newSequence.size() == 1 ? newSequence.get(0) : new Sequence(this.getPosition(), newSequence.toArray(new RValue[newSequence.size()])));
   }

   @Override
   public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
      for (int i = 0; i < this.sequence.length; i++) {
         this.sequence[i] = this.sequence[i].bindVariables(expression, false);
      }

      return this;
   }
}
