package com.sk89q.worldedit.util.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class DoubleArrayList<A, B> implements Iterable<Map.Entry<A, B>> {
   private List<A> listA = new ArrayList<>();
   private List<B> listB = new ArrayList<>();
   private boolean isReversed = false;

   public DoubleArrayList(boolean isReversed) {
      this.isReversed = isReversed;
   }

   public void put(A a, B b) {
      this.listA.add(a);
      this.listB.add(b);
   }

   public int size() {
      return this.listA.size();
   }

   public void clear() {
      this.listA.clear();
      this.listB.clear();
   }

   public Iterator<Map.Entry<A, B>> iterator(boolean reversed) {
      return (Iterator<Map.Entry<A, B>>)(reversed
         ? new DoubleArrayList.ReverseEntryIterator(this.listA.listIterator(this.listA.size()), this.listB.listIterator(this.listB.size()))
         : new DoubleArrayList.ForwardEntryIterator(this.listA.iterator(), this.listB.iterator()));
   }

   @Override
   public Iterator<Map.Entry<A, B>> iterator() {
      return this.iterator(this.isReversed);
   }

   public class Entry implements Map.Entry<A, B> {
      private A key;
      private B value;

      private Entry(A key, B value) {
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

   public class ForwardEntryIterator implements Iterator<Map.Entry<A, B>> {
      private Iterator<A> keyIterator;
      private Iterator<B> valueIterator;

      public ForwardEntryIterator(Iterator<A> keyIterator, Iterator<B> valueIterator) {
         this.keyIterator = keyIterator;
         this.valueIterator = valueIterator;
      }

      @Override
      public boolean hasNext() {
         return this.keyIterator.hasNext();
      }

      public Map.Entry<A, B> next() throws NoSuchElementException {
         return DoubleArrayList.this.new Entry(this.keyIterator.next(), this.valueIterator.next());
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }
   }

   public class ReverseEntryIterator implements Iterator<Map.Entry<A, B>> {
      private ListIterator<A> keyIterator;
      private ListIterator<B> valueIterator;

      public ReverseEntryIterator(ListIterator<A> keyIterator, ListIterator<B> valueIterator) {
         this.keyIterator = keyIterator;
         this.valueIterator = valueIterator;
      }

      @Override
      public boolean hasNext() {
         return this.keyIterator.hasPrevious();
      }

      public Map.Entry<A, B> next() throws NoSuchElementException {
         return DoubleArrayList.this.new Entry(this.keyIterator.previous(), this.valueIterator.previous());
      }

      @Override
      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}
