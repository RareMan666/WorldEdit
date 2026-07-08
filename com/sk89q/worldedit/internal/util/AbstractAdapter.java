package com.sk89q.worldedit.internal.util;

import com.google.common.base.Preconditions;

public abstract class AbstractAdapter<E> {
   private final E object;

   public AbstractAdapter(E object) {
      Preconditions.checkNotNull(object);
      this.object = object;
   }

   public E getHandle() {
      return this.object;
   }
}
