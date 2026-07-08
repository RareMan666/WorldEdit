package com.sk89q.worldedit.history.changeset;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.sk89q.worldedit.history.change.Change;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayListHistory implements ChangeSet {
   private final List<Change> changes = new ArrayList<>();

   @Override
   public void add(Change change) {
      Preconditions.checkNotNull(change);
      this.changes.add(change);
   }

   @Override
   public Iterator<Change> backwardIterator() {
      return Lists.reverse(this.changes).iterator();
   }

   @Override
   public Iterator<Change> forwardIterator() {
      return this.changes.iterator();
   }

   @Override
   public int size() {
      return this.changes.size();
   }
}
