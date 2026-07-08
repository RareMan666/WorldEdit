package com.sk89q.worldedit.history;

import com.sk89q.worldedit.extent.Extent;
import javax.annotation.Nullable;

public class UndoContext {
   private Extent extent;

   @Nullable
   public Extent getExtent() {
      return this.extent;
   }

   public void setExtent(@Nullable Extent extent) {
      this.extent = extent;
   }
}
