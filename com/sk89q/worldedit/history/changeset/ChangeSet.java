package com.sk89q.worldedit.history.changeset;

import com.sk89q.worldedit.history.change.Change;
import java.util.Iterator;

public interface ChangeSet {
   void add(Change var1);

   Iterator<Change> backwardIterator();

   Iterator<Change> forwardIterator();

   int size();
}
