package com.sk89q.worldedit.function.operation;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.history.UndoContext;
import com.sk89q.worldedit.history.change.Change;
import com.sk89q.worldedit.history.changeset.ChangeSet;
import java.util.Iterator;
import java.util.List;

public class ChangeSetExecutor implements Operation {
   private final Iterator<Change> iterator;
   private final ChangeSetExecutor.Type type;
   private final UndoContext context;

   private ChangeSetExecutor(ChangeSet changeSet, ChangeSetExecutor.Type type, UndoContext context) {
      Preconditions.checkNotNull(changeSet);
      Preconditions.checkNotNull(type);
      Preconditions.checkNotNull(context);
      this.type = type;
      this.context = context;
      if (type == ChangeSetExecutor.Type.UNDO) {
         this.iterator = changeSet.backwardIterator();
      } else {
         this.iterator = changeSet.forwardIterator();
      }
   }

   @Override
   public Operation resume(RunContext run) throws WorldEditException {
      while (this.iterator.hasNext()) {
         Change change = this.iterator.next();
         if (this.type == ChangeSetExecutor.Type.UNDO) {
            change.undo(this.context);
         } else {
            change.redo(this.context);
         }
      }

      return null;
   }

   @Override
   public void cancel() {
   }

   @Override
   public void addStatusMessages(List<String> messages) {
   }

   public static ChangeSetExecutor createUndo(ChangeSet changeSet, UndoContext context) {
      return new ChangeSetExecutor(changeSet, ChangeSetExecutor.Type.UNDO, context);
   }

   public static ChangeSetExecutor createRedo(ChangeSet changeSet, UndoContext context) {
      return new ChangeSetExecutor(changeSet, ChangeSetExecutor.Type.REDO, context);
   }

   public static enum Type {
      UNDO,
      REDO;
   }
}
