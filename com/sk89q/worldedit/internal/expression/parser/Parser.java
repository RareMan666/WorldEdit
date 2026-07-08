package com.sk89q.worldedit.internal.expression.parser;

import com.sk89q.worldedit.internal.expression.Expression;
import com.sk89q.worldedit.internal.expression.Identifiable;
import com.sk89q.worldedit.internal.expression.lexer.tokens.IdentifierToken;
import com.sk89q.worldedit.internal.expression.lexer.tokens.KeywordToken;
import com.sk89q.worldedit.internal.expression.lexer.tokens.NumberToken;
import com.sk89q.worldedit.internal.expression.lexer.tokens.OperatorToken;
import com.sk89q.worldedit.internal.expression.lexer.tokens.Token;
import com.sk89q.worldedit.internal.expression.runtime.Break;
import com.sk89q.worldedit.internal.expression.runtime.Conditional;
import com.sk89q.worldedit.internal.expression.runtime.Constant;
import com.sk89q.worldedit.internal.expression.runtime.For;
import com.sk89q.worldedit.internal.expression.runtime.Function;
import com.sk89q.worldedit.internal.expression.runtime.Functions;
import com.sk89q.worldedit.internal.expression.runtime.LValue;
import com.sk89q.worldedit.internal.expression.runtime.RValue;
import com.sk89q.worldedit.internal.expression.runtime.Return;
import com.sk89q.worldedit.internal.expression.runtime.Sequence;
import com.sk89q.worldedit.internal.expression.runtime.SimpleFor;
import com.sk89q.worldedit.internal.expression.runtime.Switch;
import com.sk89q.worldedit.internal.expression.runtime.While;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parser {
   private final List<Token> tokens;
   private int position = 0;
   private Expression expression;

   private Parser(List<Token> tokens, Expression expression) {
      this.tokens = tokens;
      this.expression = expression;
   }

   public static RValue parse(List<Token> tokens, Expression expression) throws ParserException {
      return new Parser(tokens, expression).parse();
   }

   private RValue parse() throws ParserException {
      RValue ret = this.parseStatements(false);
      if (this.position < this.tokens.size()) {
         Token token = this.peek();
         throw new ParserException(token.getPosition(), "Extra token at the end of the input: " + token);
      } else {
         ret.bindVariables(this.expression, false);
         return ret;
      }
   }

   private RValue parseStatements(boolean singleStatement) throws ParserException {
      List<RValue> statements = new ArrayList<>();

      label113:
      while (this.position < this.tokens.size()) {
         boolean expectSemicolon;
         expectSemicolon = false;
         Token current = this.peek();
         label109:
         switch (current.id()) {
            case 'k':
               String keyword = ((KeywordToken)current).value;
               switch (keyword.charAt(0)) {
                  case 'b':
                     this.position++;
                     statements.add(new Break(current.getPosition(), false));
                     break label109;
                  case 'c':
                     if (this.hasKeyword("case")) {
                        break label113;
                     }

                     this.position++;
                     statements.add(new Break(current.getPosition(), true));
                     break label109;
                  case 'd': {
                     if (this.hasKeyword("default")) {
                        break label113;
                     }

                     this.position++;
                     RValue body = this.parseStatements(true);
                     this.consumeKeyword("while");
                     RValue condition = this.parseBracket();
                     statements.add(new While(current.getPosition(), condition, body, true));
                     expectSemicolon = true;
                     break label109;
                  }
                  case 'e':
                  case 'g':
                  case 'h':
                  case 'j':
                  case 'k':
                  case 'l':
                  case 'm':
                  case 'n':
                  case 'o':
                  case 'p':
                  case 'q':
                  case 't':
                  case 'u':
                  case 'v':
                  default:
                     throw new ParserException(current.getPosition(), "Unexpected keyword '" + keyword + "'");
                  case 'f':
                     this.position++;
                     this.consumeCharacter('(');
                     int oldPosition = this.position;
                     RValue init = this.parseExpression(true);
                     if (this.peek().id() == ';') {
                        this.position++;
                        RValue conditionx = this.parseExpression(true);
                        this.consumeCharacter(';');
                        RValue increment = this.parseExpression(true);
                        this.consumeCharacter(')');
                        RValue bodyx = this.parseStatements(true);
                        statements.add(new For(current.getPosition(), init, conditionx, increment, bodyx));
                     } else {
                        this.position = oldPosition;
                        Token variableToken = this.peek();
                        if (!(variableToken instanceof IdentifierToken)) {
                           throw new ParserException(variableToken.getPosition(), "Expected identifier");
                        }

                        RValue variable = this.expression.getVariable(((IdentifierToken)variableToken).value, true);
                        if (!(variable instanceof LValue)) {
                           throw new ParserException(variableToken.getPosition(), "Expected variable");
                        }

                        this.position++;
                        Token equalsToken = this.peek();
                        if (!(equalsToken instanceof OperatorToken) || !((OperatorToken)equalsToken).operator.equals("=")) {
                           throw new ParserException(variableToken.getPosition(), "Expected '=' or a term and ';'");
                        }

                        this.position++;
                        RValue first = this.parseExpression(true);
                        this.consumeCharacter(',');
                        RValue last = this.parseExpression(true);
                        this.consumeCharacter(')');
                        RValue bodyx = this.parseStatements(true);
                        statements.add(new SimpleFor(current.getPosition(), (LValue)variable, first, last, bodyx));
                     }
                     break label109;
                  case 'i': {
                     this.position++;
                     RValue condition = this.parseBracket();
                     RValue truePart = this.parseStatements(true);
                     RValue falsePart;
                     if (this.hasKeyword("else")) {
                        this.position++;
                        falsePart = this.parseStatements(true);
                     } else {
                        falsePart = null;
                     }

                     statements.add(new Conditional(current.getPosition(), condition, truePart, falsePart));
                     break label109;
                  }
                  case 'r':
                     this.position++;
                     statements.add(new Return(current.getPosition(), this.parseExpression(true)));
                     expectSemicolon = true;
                     break label109;
                  case 's':
                     this.position++;
                     RValue parameter = this.parseBracket();
                     List<Double> values = new ArrayList<>();
                     List<RValue> caseStatements = new ArrayList<>();
                     RValue defaultCase = null;
                     this.consumeCharacter('{');

                     while (this.peek().id() != '}') {
                        if (this.position >= this.tokens.size()) {
                           throw new ParserException(current.getPosition(), "Expected '}' instead of EOF");
                        }

                        if (defaultCase != null) {
                           throw new ParserException(current.getPosition(), "Expected '}' instead of " + this.peek());
                        }

                        if (this.hasKeyword("case")) {
                           this.position++;
                           Token valueToken = this.peek();
                           if (!(valueToken instanceof NumberToken)) {
                              throw new ParserException(current.getPosition(), "Expected number instead of " + this.peek());
                           }

                           this.position++;
                           values.add(((NumberToken)valueToken).value);
                           this.consumeCharacter(':');
                           caseStatements.add(this.parseStatements(false));
                        } else {
                           if (!this.hasKeyword("default")) {
                              throw new ParserException(current.getPosition(), "Expected 'case' or 'default' instead of " + this.peek());
                           }

                           this.position++;
                           this.consumeCharacter(':');
                           defaultCase = this.parseStatements(false);
                        }
                     }

                     this.consumeCharacter('}');
                     statements.add(new Switch(current.getPosition(), parameter, values, caseStatements, defaultCase));
                     break label109;
                  case 'w': {
                     this.position++;
                     RValue condition = this.parseBracket();
                     RValue body = this.parseStatements(true);
                     statements.add(new While(current.getPosition(), condition, body, false));
                     break label109;
                  }
               }
            case '{':
               this.consumeCharacter('{');
               statements.add(this.parseStatements(false));
               this.consumeCharacter('}');
               break;
            case '}':
               break label113;
            default:
               statements.add(this.parseExpression(true));
               expectSemicolon = true;
         }

         if (expectSemicolon) {
            if (this.peek().id() != ';') {
               break;
            }

            this.position++;
         }

         if (singleStatement) {
            break;
         }
      }

      switch (statements.size()) {
         case 0:
            if (singleStatement) {
               throw new ParserException(this.peek().getPosition(), "Statement expected.");
            }

            return new Sequence(this.peek().getPosition());
         case 1:
            return statements.get(0);
         default:
            return new Sequence(this.peek().getPosition(), statements.toArray(new RValue[statements.size()]));
      }
   }

   private RValue parseExpression(boolean canBeEmpty) throws ParserException {
      LinkedList<Identifiable> halfProcessed = new LinkedList<>();
      boolean expressionStart = true;

      while (this.position < this.tokens.size()) {
         Token current = this.peek();
         switch (current.id()) {
            case '(':
               halfProcessed.add(this.parseBracket());
               expressionStart = false;
               break;
            case ')':
            case ',':
            case ';':
            case '}':
               return (RValue)(halfProcessed.isEmpty() && canBeEmpty
                  ? new Sequence(this.peek().getPosition())
                  : ParserProcessors.processExpression(halfProcessed));
            case '0':
               halfProcessed.add(new Constant(current.getPosition(), ((NumberToken)current).value));
               this.position++;
               expressionStart = false;
               break;
            case 'i':
               IdentifierToken identifierToken = (IdentifierToken)current;
               this.position++;
               Token next = this.peek();
               if (next.id() == '(') {
                  halfProcessed.add(this.parseFunctionCall(identifierToken));
               } else {
                  RValue variable = this.expression.getVariable(identifierToken.value, false);
                  if (variable == null) {
                     halfProcessed.add(new UnboundVariable(identifierToken.getPosition(), identifierToken.value));
                  } else {
                     halfProcessed.add(variable);
                  }
               }

               expressionStart = false;
               break;
            case 'o':
               if (expressionStart) {
                  halfProcessed.add(new UnaryOperator((OperatorToken)current));
               } else {
                  halfProcessed.add(current);
               }

               this.position++;
               expressionStart = true;
               break;
            default:
               halfProcessed.add(current);
               this.position++;
               expressionStart = false;
         }
      }

      return (RValue)(halfProcessed.isEmpty() && canBeEmpty ? new Sequence(this.peek().getPosition()) : ParserProcessors.processExpression(halfProcessed));
   }

   private Token peek() {
      return (Token)(this.position >= this.tokens.size()
         ? new Parser.NullToken(this.tokens.get(this.tokens.size() - 1).getPosition() + 1)
         : this.tokens.get(this.position));
   }

   private Function parseFunctionCall(IdentifierToken identifierToken) throws ParserException {
      this.consumeCharacter('(');

      try {
         if (this.peek().id() == ')') {
            this.position++;
            return Functions.getFunction(identifierToken.getPosition(), identifierToken.value);
         } else {
            List<RValue> args = new ArrayList<>();

            while (true) {
               args.add(this.parseExpression(false));
               Token current = this.peek();
               this.position++;
               switch (current.id()) {
                  case ')':
                     return Functions.getFunction(identifierToken.getPosition(), identifierToken.value, args.toArray(new RValue[args.size()]));
                  case ',':
                     break;
                  default:
                     throw new ParserException(current.getPosition(), "Unmatched opening bracket");
               }
            }
         }
      } catch (NoSuchMethodException var4) {
         throw new ParserException(identifierToken.getPosition(), "Function '" + identifierToken.value + "' not found", var4);
      }
   }

   private RValue parseBracket() throws ParserException {
      this.consumeCharacter('(');
      RValue ret = this.parseExpression(false);
      this.consumeCharacter(')');
      return ret;
   }

   private boolean hasKeyword(String keyword) {
      Token next = this.peek();
      return next instanceof KeywordToken && ((KeywordToken)next).value.equals(keyword);
   }

   private void assertCharacter(char character) throws ParserException {
      Token next = this.peek();
      if (next.id() != character) {
         throw new ParserException(next.getPosition(), "Expected '" + character + "'");
      }
   }

   private void assertKeyword(String keyword) throws ParserException {
      if (!this.hasKeyword(keyword)) {
         throw new ParserException(this.peek().getPosition(), "Expected '" + keyword + "'");
      }
   }

   private void consumeCharacter(char character) throws ParserException {
      this.assertCharacter(character);
      this.position++;
   }

   private void consumeKeyword(String keyword) throws ParserException {
      this.assertKeyword(keyword);
      this.position++;
   }

   private final class NullToken extends Token {
      private NullToken(int position) {
         super(position);
      }

      @Override
      public char id() {
         return '\u0000';
      }

      @Override
      public String toString() {
         return "NullToken";
      }
   }
}
