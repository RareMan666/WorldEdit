package com.sk89q.worldedit.function.operation;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.WorldEditException;
import java.util.List;

public class DelegateOperation implements Operation {
   private final Operation original;
   private Operation delegate;

   public DelegateOperation(Operation original, Operation delegate) {
      Preconditions.checkNotNull(original);
      Preconditions.checkNotNull(delegate);
      this.original = original;
      this.delegate = delegate;
   }

   @Override
   public Operation resume(RunContext run) throws WorldEditException {
      this.delegate = this.delegate.resume(run);
      return (Operation)(this.delegate != null ? this : this.original);
   }

   @Override
   public void cancel() {
      this.delegate.cancel();
      this.original.cancel();
   }

   @Override
   public void addStatusMessages(List<String> messages) {
      this.original.addStatusMessages(messages);
      this.delegate.addStatusMessages(messages);
   }
}
