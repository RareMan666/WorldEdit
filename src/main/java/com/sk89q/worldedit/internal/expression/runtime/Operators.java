package com.sk89q.worldedit.internal.expression.runtime;

public final class Operators {
   private static final double[] factorials = new double[171];

   private Operators() {
   }

   public static Function getOperator(int position, String name, RValue lhs, RValue rhs) throws NoSuchMethodException {
      if (lhs instanceof LValue) {
         try {
            return new Function(position, Operators.class.getMethod(name, LValue.class, RValue.class), lhs, rhs);
         } catch (NoSuchMethodException var5) {
         }
      }

      return new Function(position, Operators.class.getMethod(name, RValue.class, RValue.class), lhs, rhs);
   }

   public static Function getOperator(int position, String name, RValue argument) throws NoSuchMethodException {
      if (argument instanceof LValue) {
         try {
            return new Function(position, Operators.class.getMethod(name, LValue.class), argument);
         } catch (NoSuchMethodException var4) {
         }
      }

      return new Function(position, Operators.class.getMethod(name, RValue.class), argument);
   }

   public static double add(RValue lhs, RValue rhs) throws EvaluationException {
      return lhs.getValue() + rhs.getValue();
   }

   public static double sub(RValue lhs, RValue rhs) throws EvaluationException {
      return lhs.getValue() - rhs.getValue();
   }

   public static double mul(RValue lhs, RValue rhs) throws EvaluationException {
      return lhs.getValue() * rhs.getValue();
   }

   public static double div(RValue lhs, RValue rhs) throws EvaluationException {
      return lhs.getValue() / rhs.getValue();
   }

   public static double mod(RValue lhs, RValue rhs) throws EvaluationException {
      return lhs.getValue() % rhs.getValue();
   }

   public static double pow(RValue lhs, RValue rhs) throws EvaluationException {
      return Math.pow(lhs.getValue(), rhs.getValue());
   }

   public static double neg(RValue x) throws EvaluationException {
      return -x.getValue();
   }

   public static double not(RValue x) throws EvaluationException {
      return x.getValue() > 0.0 ? 0.0 : 1.0;
   }

   public static double inv(RValue x) throws EvaluationException {
      return ~((long)x.getValue());
   }

   public static double lth(RValue lhs, RValue rhs) throws EvaluationException {
      return lhs.getValue() < rhs.getValue() ? 1.0 : 0.0;
   }

   public static double gth(RValue lhs, RValue rhs) throws EvaluationException {
      return lhs.getValue() > rhs.getValue() ? 1.0 : 0.0;
   }

   public static double leq(RValue lhs, RValue rhs) throws EvaluationException {
      return lhs.getValue() <= rhs.getValue() ? 1.0 : 0.0;
   }

   public static double geq(RValue lhs, RValue rhs) throws EvaluationException {
      return lhs.getValue() >= rhs.getValue() ? 1.0 : 0.0;
   }

   public static double equ(RValue lhs, RValue rhs) throws EvaluationException {
      return lhs.getValue() == rhs.getValue() ? 1.0 : 0.0;
   }

   public static double neq(RValue lhs, RValue rhs) throws EvaluationException {
      return lhs.getValue() != rhs.getValue() ? 1.0 : 0.0;
   }

   public static double near(RValue lhs, RValue rhs) throws EvaluationException {
      return almostEqual2sComplement(lhs.getValue(), rhs.getValue(), 450359963L) ? 1.0 : 0.0;
   }

   public static double or(RValue lhs, RValue rhs) throws EvaluationException {
      return !(lhs.getValue() > 0.0) && !(rhs.getValue() > 0.0) ? 0.0 : 1.0;
   }

   public static double and(RValue lhs, RValue rhs) throws EvaluationException {
      return lhs.getValue() > 0.0 && rhs.getValue() > 0.0 ? 1.0 : 0.0;
   }

   public static double shl(RValue lhs, RValue rhs) throws EvaluationException {
      return (long)lhs.getValue() << (int)((long)rhs.getValue());
   }

   public static double shr(RValue lhs, RValue rhs) throws EvaluationException {
      return (long)lhs.getValue() >> (int)((long)rhs.getValue());
   }

   public static double ass(LValue lhs, RValue rhs) throws EvaluationException {
      return lhs.assign(rhs.getValue());
   }

   public static double aadd(LValue lhs, RValue rhs) throws EvaluationException {
      return lhs.assign(lhs.getValue() + rhs.getValue());
   }

   public static double asub(LValue lhs, RValue rhs) throws EvaluationException {
      return lhs.assign(lhs.getValue() - rhs.getValue());
   }

   public static double amul(LValue lhs, RValue rhs) throws EvaluationException {
      return lhs.assign(lhs.getValue() * rhs.getValue());
   }

   public static double adiv(LValue lhs, RValue rhs) throws EvaluationException {
      return lhs.assign(lhs.getValue() / rhs.getValue());
   }

   public static double amod(LValue lhs, RValue rhs) throws EvaluationException {
      return lhs.assign(lhs.getValue() % rhs.getValue());
   }

   public static double aexp(LValue lhs, RValue rhs) throws EvaluationException {
      return lhs.assign(Math.pow(lhs.getValue(), rhs.getValue()));
   }

   public static double inc(LValue x) throws EvaluationException {
      return x.assign(x.getValue() + 1.0);
   }

   public static double dec(LValue x) throws EvaluationException {
      return x.assign(x.getValue() - 1.0);
   }

   public static double postinc(LValue x) throws EvaluationException {
      double oldValue = x.getValue();
      x.assign(oldValue + 1.0);
      return oldValue;
   }

   public static double postdec(LValue x) throws EvaluationException {
      double oldValue = x.getValue();
      x.assign(oldValue - 1.0);
      return oldValue;
   }

   public static double fac(RValue x) throws EvaluationException {
      int n = (int)x.getValue();
      if (n < 0) {
         return 0.0;
      } else {
         return n >= factorials.length ? Double.POSITIVE_INFINITY : factorials[n];
      }
   }

   private static boolean almostEqual2sComplement(double a, double b, long maxUlps) {
      long aLong = Double.doubleToRawLongBits(a);
      if (aLong < 0L) {
         aLong = Long.MIN_VALUE - aLong;
      }

      long bLong = Double.doubleToRawLongBits(b);
      if (bLong < 0L) {
         bLong = Long.MIN_VALUE - bLong;
      }

      long longDiff = Math.abs(aLong - bLong);
      return longDiff <= maxUlps;
   }

   static {
      double accum = 1.0;
      factorials[0] = 1.0;

      for (int i = 1; i < factorials.length; i++) {
         factorials[i] = accum *= i;
      }
   }
}
