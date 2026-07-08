package com.sk89q.worldedit.internal.cui;

public class SelectionMinMaxEvent implements CUIEvent {
   protected final int min;
   protected final int max;

   public SelectionMinMaxEvent(int min, int max) {
      this.min = min;
      this.max = max;
   }

   @Override
   public String getTypeId() {
      return "mm";
   }

   @Override
   public String[] getParameters() {
      return new String[]{String.valueOf(this.min), String.valueOf(this.max)};
   }
}
