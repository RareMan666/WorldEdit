package com.sk89q.worldedit.util.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

public class TupleArrayList<A, B> extends ArrayList<Entry<A, B>> {
   public void put(A a, B b) {
      this.add(new TupleArrayList.Tuple<>(a, b));
   }

   public Iterator<Entry<A, B>> iterator(boolean reverse) {
      return reverse ? this.reverseIterator() : this.iterator();
   }

   @Override
   public Iterator<Entry<A, B>> iterator() {
      return FastListIterator.forwardIterator(this);
   }

   public Iterator<Entry<A, B>> reverseIterator() {
      return FastListIterator.reverseIterator(this);
   }

   private static class Tuple<A, B> implements Entry<A, B> {
      private A key;
      private B value;

      private Tuple(A key, B value) {
         this.key = key;
         this.value = value;
      }

      @Override
      public A getKey() {
         return this.key;
      }

      @Override
      public B getValue() {
         return this.value;
      }

      @Override
      public B setValue(B value) {
         throw new UnsupportedOperationException();
      }
   }
}
