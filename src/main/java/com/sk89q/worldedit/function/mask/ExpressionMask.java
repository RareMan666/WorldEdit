package com.sk89q.worldedit.function.mask;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.ExpressionException;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;
import com.sk89q.worldedit.regions.shape.WorldEditExpressionEnvironment;
import javax.annotation.Nullable;

public class ExpressionMask extends AbstractMask {
   private final Expression expression;

   public ExpressionMask(String expression) throws ExpressionException {
      Preconditions.checkNotNull(expression);
      this.expression = Expression.compile(expression, "x", "y", "z");
   }

   public ExpressionMask(Expression expression) {
      Preconditions.checkNotNull(expression);
      this.expression = expression;
   }

   @Override
   public boolean test(Vector vector) {
      try {
         if (this.expression.getEnvironment() instanceof WorldEditExpressionEnvironment) {
            ((WorldEditExpressionEnvironment)this.expression.getEnvironment()).setCurrentBlock(vector);
         }

         return this.expression.evaluate(vector.getX(), vector.getY(), vector.getZ()) > 0.0;
      } catch (EvaluationException var3) {
         return false;
      }
   }

   @Nullable
   @Override
   public Mask2D toMask2D() {
      return new ExpressionMask2D(this.expression);
   }
}
