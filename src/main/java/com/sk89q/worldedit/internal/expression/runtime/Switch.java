package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Switch extends Node implements RValue {
   private RValue parameter;
   private final Map<Double, Integer> valueMap;
   private final RValue[] caseStatements;
   private RValue defaultCase;

   public Switch(int position, RValue parameter, List<Double> values, List<RValue> caseStatements, RValue defaultCase) {
      this(position, parameter, invertList(values), caseStatements, defaultCase);
   }

   private static Map<Double, Integer> invertList(List<Double> values) {
      Map<Double, Integer> valueMap = new HashMap<>();

      for (int i = 0; i < values.size(); i++) {
         valueMap.put(values.get(i), i);
      }

      return valueMap;
   }

   private Switch(int position, RValue parameter, Map<Double, Integer> valueMap, List<RValue> caseStatements, RValue defaultCase) {
      super(position);
      this.parameter = parameter;
      this.valueMap = valueMap;
      this.caseStatements = caseStatements.toArray(new RValue[caseStatements.size()]);
      this.defaultCase = defaultCase;
   }

   @Override
   public char id() {
      return 'W';
   }

   @Override
   public double getValue() throws EvaluationException {
      double parameter = this.parameter.getValue();

      try {
         double ret = 0.0;
         Integer index = this.valueMap.get(parameter);
         if (index != null) {
            for (int i = index; i < this.caseStatements.length; i++) {
               ret = this.caseStatements[i].getValue();
            }
         }

         return this.defaultCase == null ? ret : this.defaultCase.getValue();
      } catch (BreakException var8) {
         if (var8.doContinue) {
            throw var8;
         } else {
            return 0.0;
         }
      }
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("switch (");
      sb.append(this.parameter);
      sb.append(") { ");

      for (int i = 0; i < this.caseStatements.length; i++) {
         RValue caseStatement = this.caseStatements[i];
         sb.append("case ");

         for (Entry<Double, Integer> entry : this.valueMap.entrySet()) {
            if (entry.getValue() == i) {
               sb.append(entry.getKey());
               break;
            }
         }

         sb.append(": ");
         sb.append(caseStatement);
         sb.append(' ');
      }

      if (this.defaultCase != null) {
         sb.append("default: ");
         sb.append(this.defaultCase);
         sb.append(' ');
      }

      sb.append("}");
      return sb.toString();
   }

   @Override
   public RValue optimize() throws EvaluationException {
      RValue optimizedParameter = this.parameter.optimize();
      List<RValue> newSequence = new ArrayList<>();
      if (optimizedParameter instanceof Constant) {
         double parameter = optimizedParameter.getValue();
         Integer index = this.valueMap.get(parameter);
         if (index == null) {
            return (RValue)(this.defaultCase == null ? new Constant(this.getPosition(), 0.0) : this.defaultCase.optimize());
         } else {
            boolean breakDetected = false;

            for (int i = index; i < this.caseStatements.length && !breakDetected; i++) {
               RValue invokable = this.caseStatements[i].optimize();
               if (invokable instanceof Sequence) {
                  for (RValue subInvokable : ((Sequence)invokable).sequence) {
                     if (subInvokable instanceof Break) {
                        breakDetected = true;
                        break;
                     }

                     newSequence.add(subInvokable);
                  }
               } else {
                  newSequence.add(invokable);
               }
            }

            if (this.defaultCase != null && !breakDetected) {
               RValue invokable = this.defaultCase.optimize();
               if (invokable instanceof Sequence) {
                  Collections.addAll(newSequence, ((Sequence)invokable).sequence);
               } else {
                  newSequence.add(invokable);
               }
            }

            return new Switch(this.getPosition(), optimizedParameter, Collections.singletonMap(parameter, 0), newSequence, null);
         }
      } else {
         Map<Double, Integer> newValueMap = new HashMap<>();
         Map<Integer, Double> backMap = new HashMap<>();

         for (Entry<Double, Integer> entry : this.valueMap.entrySet()) {
            backMap.put(entry.getValue(), entry.getKey());
         }

         for (int ix = 0; ix < this.caseStatements.length; ix++) {
            RValue invokable = this.caseStatements[ix].optimize();
            Double caseValue = backMap.get(ix);
            if (caseValue != null) {
               newValueMap.put(caseValue, newSequence.size());
            }

            if (invokable instanceof Sequence) {
               Collections.addAll(newSequence, ((Sequence)invokable).sequence);
            } else {
               newSequence.add(invokable);
            }
         }

         return new Switch(this.getPosition(), optimizedParameter, newValueMap, newSequence, this.defaultCase.optimize());
      }
   }

   @Override
   public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
      this.parameter = this.parameter.bindVariables(expression, false);

      for (int i = 0; i < this.caseStatements.length; i++) {
         this.caseStatements[i] = this.caseStatements[i].bindVariables(expression, false);
      }

      this.defaultCase = this.defaultCase.bindVariables(expression, false);
      return this;
   }
}
