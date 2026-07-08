package com.sk89q.worldedit.internal.cui;

public class SelectionShapeEvent implements CUIEvent {
   protected final String shapeName;

   public SelectionShapeEvent(String shapeName) {
      this.shapeName = shapeName;
   }

   @Override
   public String getTypeId() {
      return "s";
   }

   @Override
   public String[] getParameters() {
      return new String[]{this.shapeName};
   }
}
