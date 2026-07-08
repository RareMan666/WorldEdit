package com.sk89q.worldedit.history.changeset;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.history.change.BlockChange;
import com.sk89q.worldedit.history.change.Change;
import com.sk89q.worldedit.util.collection.TupleArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

public class BlockOptimizedHistory extends ArrayListHistory {
   private final TupleArrayList<BlockVector, BaseBlock> previous = new TupleArrayList<>();
   private final TupleArrayList<BlockVector, BaseBlock> current = new TupleArrayList<>();

   @Override
   public void add(Change change) {
      Preconditions.checkNotNull(change);
      if (change instanceof BlockChange) {
         BlockChange blockChange = (BlockChange)change;
         BlockVector position = blockChange.getPosition();
         this.previous.put(position, blockChange.getPrevious());
         this.current.put(position, blockChange.getCurrent());
      } else {
         super.add(change);
      }
   }

   @Override
   public Iterator<Change> forwardIterator() {
      return Iterators.concat(super.forwardIterator(), Iterators.transform(this.current.iterator(), this.createTransform()));
   }

   @Override
   public Iterator<Change> backwardIterator() {
      return Iterators.concat(super.backwardIterator(), Iterators.transform(this.previous.iterator(true), this.createTransform()));
   }

   @Override
   public int size() {
      return super.size() + this.previous.size();
   }

   private Function<Entry<BlockVector, BaseBlock>, Change> createTransform() {
      return new Function<Entry<BlockVector, BaseBlock>, Change>() {
         public Change apply(Entry<BlockVector, BaseBlock> entry) {
            return new BlockChange(entry.getKey(), entry.getValue(), entry.getValue());
         }
      };
   }
}
