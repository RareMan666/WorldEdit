package com.sk89q.jchronic.tags;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.utils.Token;
import java.util.List;

public class Separator extends Tag<Separator.SeparatorType> {
   public Separator(Separator.SeparatorType type) {
      super(type);
   }

   public static List<Token> scan(List<Token> tokens, Options options) {
      for (Token token : tokens) {
         Separator t = SeparatorComma.scan(token, options);
         if (t != null) {
            token.tag(t);
         }

         t = SeparatorSlashOrDash.scan(token, options);
         if (t != null) {
            token.tag(t);
         }

         t = SeparatorAt.scan(token, options);
         if (t != null) {
            token.tag(t);
         }

         t = SeparatorIn.scan(token, options);
         if (t != null) {
            token.tag(t);
         }
      }

      return tokens;
   }

   @Override
   public String toString() {
      return "separator";
   }

   public static enum SeparatorType {
      COMMA,
      DASH,
      SLASH,
      AT,
      NEWLINE,
      IN;
   }
}
