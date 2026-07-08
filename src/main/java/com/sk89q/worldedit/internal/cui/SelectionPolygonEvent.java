package com.sk89q.worldedit.internal.cui;

public class SelectionPolygonEvent implements CUIEvent {
   protected final int[] vertices;

   public SelectionPolygonEvent(int... vertices) {
      this.vertices = vertices;
   }

   @Override
   public String getTypeId() {
      return "poly";
   }

   @Override
   public String[] getParameters() {
      String[] ret = new String[this.vertices.length];
      int i = 0;

      for (int vertex : this.vertices) {
         ret[i++] = String.valueOf(vertex);
      }

      return ret;
   }
}
