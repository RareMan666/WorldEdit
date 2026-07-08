package com.sk89q.worldedit.util.collection;

import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class FastListIterator<E> implements Iterator<E> {
   private final List<E> list;
   private int index;
   private final int size;
   private final int increment;

   private FastListIterator(List<E> list, int index, int size, int increment) {
      Preconditions.checkNotNull(list);
      Preconditions.checkArgument(size >= 0, "size >= 0 required");
      Preconditions.checkArgument(index >= 0, "index >= 0 required");
      this.list = list;
      this.index = index;
      this.size = size;
      this.increment = increment;
   }

   @Override
   public boolean hasNext() {
      return this.index >= 0 && this.index < this.size;
   }

   @Override
   public E next() {
      if (this.hasNext()) {
         E entry = this.list.get(this.index);
         this.index = this.index + this.increment;
         return entry;
      } else {
         throw new NoSuchElementException();
      }
   }

   @Override
   public void remove() {
      throw new UnsupportedOperationException("Not supported");
   }

   public static <E> Iterator<E> forwardIterator(List<E> list) {
      return new FastListIterator<>(list, 0, list.size(), 1);
   }

   public static <E> Iterator<E> reverseIterator(List<E> list) {
      return !list.isEmpty() ? new FastListIterator<>(list, list.size() - 1, list.size(), -1) : new FastListIterator<>(list, 0, 0, -1);
   }
}
