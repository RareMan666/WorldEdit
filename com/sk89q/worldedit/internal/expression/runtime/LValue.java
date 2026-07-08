package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.parser.ParserException;

public interface LValue extends RValue {
   double assign(double var1) throws EvaluationException;

   LValue optimize() throws EvaluationException;

   LValue bindVariables(Expression var1, boolean var2) throws ParserException;
}
