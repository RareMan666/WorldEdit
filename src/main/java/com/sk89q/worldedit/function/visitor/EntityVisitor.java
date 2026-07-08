package com.sk89q.worldedit.function.visitor;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.function.EntityFunction;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.RunContext;
import java.util.Iterator;
import java.util.List;

public class EntityVisitor implements Operation {
   private final Iterator<? extends Entity> iterator;
   private final EntityFunction function;
   private int affected = 0;

   public EntityVisitor(Iterator<? extends Entity> iterator, EntityFunction function) {
      Preconditions.checkNotNull(iterator);
      Preconditions.checkNotNull(function);
      this.iterator = iterator;
      this.function = function;
   }

   public int getAffected() {
      return this.affected;
   }

   @Override
   public Operation resume(RunContext run) throws WorldEditException {
      while (this.iterator.hasNext()) {
         if (this.function.apply(this.iterator.next())) {
            this.affected++;
         }
      }

      return null;
   }

   @Override
   public void cancel() {
   }

   @Override
   public void addStatusMessages(List<String> messages) {
      messages.add(this.getAffected() + " entities affected");
   }
}
