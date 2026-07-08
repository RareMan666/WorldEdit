package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.Options;
import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.tags.Tag;
import com.sk89q.jchronic.utils.Span;
import com.sk89q.jchronic.utils.Token;
import java.util.List;

public abstract class Repeater<T> extends Tag<T> implements Comparable<Repeater<?>> {
   public Repeater(T type) {
      super(type);
   }

   public static List<Token> scan(List<Token> tokens) {
      return scan(tokens, new Options());
   }

   public static List<Token> scan(List<Token> tokens, Options options) {
      for (Token token : tokens) {
         Tag<?> t = RepeaterMonthName.scan(token);
         if (t != null) {
            token.tag(t);
         }

         t = RepeaterDayName.scan(token);
         if (t != null) {
            token.tag(t);
         }

         t = RepeaterDayPortion.scan(token);
         if (t != null) {
            token.tag(t);
         }

         t = RepeaterTime.scan(token, tokens, options);
         if (t != null) {
            token.tag(t);
         }

         t = RepeaterUnit.scan(token);
         if (t != null) {
            token.tag(t);
         }
      }

      return tokens;
   }

   public int compareTo(Repeater<?> other) {
      return Integer.valueOf(this.getWidth()).compareTo(other.getWidth());
   }

   public abstract int getWidth();

   public Span nextSpan(Pointer.PointerType pointer) {
      if (this.getNow() == null) {
         throw new IllegalStateException("Start point must be set before calling #next");
      } else {
         return this._nextSpan(pointer);
      }
   }

   protected abstract Span _nextSpan(Pointer.PointerType var1);

   public Span thisSpan(Pointer.PointerType pointer) {
      if (this.getNow() == null) {
         throw new IllegalStateException("Start point must be set before calling #this");
      } else {
         return this._thisSpan(pointer);
      }
   }

   protected abstract Span _thisSpan(Pointer.PointerType var1);

   public abstract Span getOffset(Span var1, int var2, Pointer.PointerType var3);

   @Override
   public String toString() {
      return "repeater";
   }
}
