package com.sk89q.jchronic.tags;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.utils.Token;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class Pointer extends Tag<Pointer.PointerType> {
   private static final Pattern IN_PATTERN = Pattern.compile("\\bin\\b");
   private static final Pattern FUTURE_PATTERN = Pattern.compile("\\bfuture\\b");
   private static final Pattern PAST_PATTERN = Pattern.compile("\\bpast\\b");

   public Pointer(Pointer.PointerType type) {
      super(type);
   }

   public static List<Token> scan(List<Token> tokens, Options options) {
      for (Token token : tokens) {
         Pointer t = scanForAll(token, options);
         if (t != null) {
            token.tag(t);
         }
      }

      return tokens;
   }

   public static Pointer scanForAll(Token token, Options options) {
      Map<Pattern, Pointer.PointerType> scanner = new HashMap<>();
      scanner.put(PAST_PATTERN, Pointer.PointerType.PAST);
      scanner.put(FUTURE_PATTERN, Pointer.PointerType.FUTURE);
      scanner.put(IN_PATTERN, Pointer.PointerType.FUTURE);

      for (Pattern scannerItem : scanner.keySet()) {
         if (scannerItem.matcher(token.getWord()).matches()) {
            return new Pointer(scanner.get(scannerItem));
         }
      }

      return null;
   }

   @Override
   public String toString() {
      return "pointer-" + this.getType();
   }

   public static enum PointerType {
      PAST,
      FUTURE,
      NONE;
   }
}
