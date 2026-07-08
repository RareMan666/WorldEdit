package com.sk89q.minecraft.util.commands;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.annotation.Nullable;

public class CommandException extends Exception {
   private List<String> commandStack = new ArrayList<>();

   public CommandException() {
   }

   public CommandException(String message) {
      super(message);
   }

   public CommandException(String message, Throwable t) {
      super(message, t);
   }

   public CommandException(Throwable t) {
      super(t);
   }

   public void prependStack(String name) {
      this.commandStack.add(name);
   }

   public String getCommandUsed(String prefix, @Nullable String spacedSuffix) {
      Preconditions.checkNotNull(prefix);
      StringBuilder builder = new StringBuilder();
      if (prefix != null) {
         builder.append(prefix);
      }

      for (ListIterator<String> li = this.commandStack.listIterator(this.commandStack.size()); li.hasPrevious(); builder.append(li.previous())) {
         if (li.previousIndex() != this.commandStack.size() - 1) {
            builder.append(" ");
         }
      }

      if (spacedSuffix != null) {
         if (builder.length() > 0) {
            builder.append(" ");
         }

         builder.append(spacedSuffix);
      }

      return builder.toString().trim();
   }
}
