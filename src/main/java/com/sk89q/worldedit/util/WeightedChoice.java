package com.sk89q.worldedit.util;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class WeightedChoice<T> {
   private final Function<T, ? extends Number> function;
   private double target;
   private double best;
   private T current;

   public WeightedChoice(Function<T, ? extends Number> function, double target) {
      Preconditions.checkNotNull(function);
      this.function = function;
      this.target = target;
   }

   public void consider(T object) {
      Preconditions.checkNotNull(object);
      Number value = (Number)Preconditions.checkNotNull(this.function.apply(object));
      if (value != null) {
         double distance = Math.abs(this.target - value.doubleValue());
         if (this.current == null || distance <= this.best) {
            this.best = distance;
            this.current = object;
         }
      }
   }

   public Optional<WeightedChoice.Choice<T>> getChoice() {
      return this.current != null ? Optional.of(new WeightedChoice.Choice(this.current, this.best)) : Optional.absent();
   }

   public static class Choice<T> {
      private final T object;
      private final double value;

      private Choice(T object, double value) {
         this.object = object;
         this.value = value;
      }

      public T getValue() {
         return this.object;
      }

      public double getScore() {
         return this.value;
      }
   }
}
