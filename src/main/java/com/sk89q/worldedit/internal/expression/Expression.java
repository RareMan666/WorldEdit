package com.sk89q.worldedit.internal.expression;

import com.sk89q.worldedit.internal.expression.lexer.Lexer;
import com.sk89q.worldedit.internal.expression.lexer.tokens.Token;
import com.sk89q.worldedit.internal.expression.parser.Parser;
import com.sk89q.worldedit.internal.expression.runtime.Constant;
import com.sk89q.worldedit.internal.expression.runtime.EvaluationException;
import com.sk89q.worldedit.internal.expression.runtime.ExpressionEnvironment;
import com.sk89q.worldedit.internal.expression.runtime.Functions;
import com.sk89q.worldedit.internal.expression.runtime.RValue;
import com.sk89q.worldedit.internal.expression.runtime.ReturnException;
import com.sk89q.worldedit.internal.expression.runtime.Variable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Expression {
   private static final ThreadLocal<Stack<Expression>> instance = new ThreadLocal<>();
   private final Map<String, RValue> variables = new HashMap<>();
   private final String[] variableNames;
   private RValue root;
   private final Functions functions = new Functions();
   private ExpressionEnvironment environment;

   public static Expression compile(String expression, String... variableNames) throws ExpressionException {
      return new Expression(expression, variableNames);
   }

   private Expression(String expression, String... variableNames) throws ExpressionException {
      this(Lexer.tokenize(expression), variableNames);
   }

   private Expression(List<Token> tokens, String... variableNames) throws ExpressionException {
      this.variableNames = variableNames;
      this.variables.put("e", new Constant(-1, Math.E));
      this.variables.put("pi", new Constant(-1, Math.PI));
      this.variables.put("true", new Constant(-1, 1.0));
      this.variables.put("false", new Constant(-1, 0.0));

      for (String variableName : variableNames) {
         if (this.variables.containsKey(variableName)) {
            throw new ExpressionException(-1, "Tried to overwrite identifier '" + variableName + "'");
         }

         this.variables.put(variableName, new Variable(0.0));
      }

      this.root = Parser.parse(tokens, this);
   }

   public double evaluate(double... values) throws EvaluationException {
      for (int i = 0; i < values.length; i++) {
         String variableName = this.variableNames[i];
         RValue invokable = this.variables.get(variableName);
         if (!(invokable instanceof Variable)) {
            throw new EvaluationException(invokable.getPosition(), "Tried to assign constant " + variableName + ".");
         }

         ((Variable)invokable).value = values[i];
      }

      this.pushInstance();

      double var7;
      try {
         return this.root.getValue();
      } catch (ReturnException var12) {
         var7 = var12.getValue();
      } finally {
         this.popInstance();
      }

      return var7;
   }

   public void optimize() throws EvaluationException {
      this.root = this.root.optimize();
   }

   @Override
   public String toString() {
      return this.root.toString();
   }

   public RValue getVariable(String name, boolean create) {
      RValue variable = this.variables.get(name);
      if (variable == null && create) {
         this.variables.put(name, variable = new Variable(0.0));
      }

      return variable;
   }

   public static Expression getInstance() {
      return instance.get().peek();
   }

   private void pushInstance() {
      Stack<Expression> foo = instance.get();
      if (foo == null) {
         instance.set(foo = new Stack<>());
      }

      foo.push(this);
   }

   private void popInstance() {
      Stack<Expression> foo = instance.get();
      foo.pop();
      if (foo.isEmpty()) {
         instance.set(null);
      }
   }

   public Functions getFunctions() {
      return this.functions;
   }

   public ExpressionEnvironment getEnvironment() {
      return this.environment;
   }

   public void setEnvironment(ExpressionEnvironment environment) {
      this.environment = environment;
   }
}
