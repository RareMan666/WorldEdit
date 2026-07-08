package com.sk89q.jchronic.repeaters;

import com.sk89q.jchronic.tags.Pointer;
import com.sk89q.jchronic.utils.Span;

public class RepeaterSeasonName extends Repeater<Object> {
   public RepeaterSeasonName(Object type) {
      super(type);
   }

   @Override
   protected Span _nextSpan(Pointer.PointerType pointer) {
      throw new IllegalStateException("Not implemented.");
   }

   @Override
   protected Span _thisSpan(Pointer.PointerType pointer) {
      throw new IllegalStateException("Not implemented.");
   }

   @Override
   public Span getOffset(Span span, int amount, Pointer.PointerType pointer) {
      throw new IllegalStateException("Not implemented.");
   }

   @Override
   public int getWidth() {
      return 7862400;
   }

   @Override
   public String toString() {
      return super.toString() + "-season-" + this.getType();
   }
}
