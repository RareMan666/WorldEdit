package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;
import java.lang.reflect.Method;

public class LValueFunction extends Function implements LValue {
   private final Object[] setterArgs;
   private final Method setter;

   LValueFunction(int position, Method getter, Method setter, RValue... args) {
      super(position, getter, args);

      assert getter.isAnnotationPresent(Function.Dynamic.class);

      this.setterArgs = new Object[args.length + 1];
      System.arraycopy(args, 0, this.setterArgs, 0, args.length);
      this.setter = setter;
   }

   @Override
   public char id() {
      return 'l';
   }

   @Override
   public double assign(double value) throws EvaluationException {
      this.setterArgs[this.setterArgs.length - 1] = value;
      return invokeMethod(this.setter, this.setterArgs);
   }

   @Override
   public LValue optimize() throws EvaluationException {
      RValue optimized = super.optimize();
      if (optimized == this) {
         return this;
      } else {
         return (LValue)(optimized instanceof Function
            ? new LValueFunction(optimized.getPosition(), this.method, this.setter, ((Function)optimized).args)
            : (LValue)optimized);
      }
   }

   @Override
   public LValue bindVariables(Expression expression, boolean preferLValue) throws ParserException {
      super.bindVariables(expression, preferLValue);
      return this;
   }
}
