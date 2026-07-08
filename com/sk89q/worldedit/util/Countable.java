package com.sk89q.worldedit.util;

public class Countable<T> implements Comparable<Countable<T>> {
   private T id;
   private int amount;

   public Countable(T id, int amount) {
      this.id = id;
      this.amount = amount;
   }

   public T getID() {
      return this.id;
   }

   public void setID(T id) {
      this.id = id;
   }

   public int getAmount() {
      return this.amount;
   }

   public void setAmount(int amount) {
      this.amount = amount;
   }

   public void decrement() {
      this.amount--;
   }

   public void increment() {
      this.amount++;
   }

   public int compareTo(Countable<T> other) {
      if (this.amount > other.amount) {
         return 1;
      } else {
         return this.amount == other.amount ? 0 : -1;
      }
   }
}
