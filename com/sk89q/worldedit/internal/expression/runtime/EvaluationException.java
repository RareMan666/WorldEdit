package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.ExpressionException;

public class EvaluationException extends ExpressionException {
   public EvaluationException(int position) {
      super(position, getPrefix(position));
   }

   public EvaluationException(int position, String message, Throwable cause) {
      super(position, getPrefix(position) + ": " + message, cause);
   }

   public EvaluationException(int position, String message) {
      super(position, getPrefix(position) + ": " + message);
   }

   public EvaluationException(int position, Throwable cause) {
      super(position, getPrefix(position), cause);
   }

   private static String getPrefix(int position) {
      return position < 0 ? "Evaluation error" : "Evaluation error at " + (position + 1);
   }
}
