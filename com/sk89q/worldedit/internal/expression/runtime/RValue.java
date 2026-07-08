package com.sk89q.worldedit.internal.expression.runtime;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.Identifiable;
import com.sk89q.worldedit.internal.expression.parser.ParserException;

public interface RValue extends Identifiable {
   double getValue() throws EvaluationException;

   RValue optimize() throws EvaluationException;

   RValue bindVariables(Expression var1, boolean var2) throws ParserException;
}
