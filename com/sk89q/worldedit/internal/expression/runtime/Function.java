package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Function extends Node {
   final Method method;
   final RValue[] args;

   Function(int position, Method method, RValue... args) {
      super(position);
      this.method = method;
      this.args = args;
   }

   @Override
   public final double getValue() throws EvaluationException {
      return invokeMethod(this.method, this.args);
   }

   protected static double invokeMethod(Method method, Object[] args) throws EvaluationException {
      try {
         return (Double)method.invoke(null, args);
      } catch (InvocationTargetException var3) {
         if (var3.getTargetException() instanceof EvaluationException) {
            throw (EvaluationException)var3.getTargetException();
         } else {
            throw new EvaluationException(-1, "Exception caught while evaluating expression", var3.getTargetException());
         }
      } catch (IllegalAccessException var4) {
         throw new EvaluationException(-1, "Internal error while evaluating expression", var4);
      }
   }

   @Override
   public String toString() {
      StringBuilder ret = new StringBuilder(this.method.getName()).append('(');
      boolean first = true;

      for (Object obj : this.args) {
         if (!first) {
            ret.append(", ");
         }

         first = false;
         ret.append(obj);
      }

      return ret.append(')').toString();
   }

   @Override
   public char id() {
      return 'f';
   }

   @Override
   public RValue optimize() throws EvaluationException {
      RValue[] optimizedArgs = new RValue[this.args.length];
      boolean optimizable = !this.method.isAnnotationPresent(Function.Dynamic.class);
      int position = this.getPosition();

      for (int i = 0; i < this.args.length; i++) {
         RValue optimized = optimizedArgs[i] = this.args[i].optimize();
         if (!(optimized instanceof Constant)) {
            optimizable = false;
         }

         if (optimized.getPosition() < position) {
            position = optimized.getPosition();
         }
      }

      return (RValue)(optimizable ? new Constant(position, invokeMethod(this.method, optimizedArgs)) : new Function(position, this.method, optimizedArgs));
   }

   @Override
   public RValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
      Class<?>[] parameters = this.method.getParameterTypes();

      for (int i = 0; i < this.args.length; i++) {
         boolean argumentPrefersLValue = LValue.class.isAssignableFrom(parameters[i]);
         this.args[i] = this.args[i].bindVariables(expression, argumentPrefersLValue);
      }

      return this;
   }

   @Retention(RetentionPolicy.RUNTIME)
   public @interface Dynamic {
   }
}
