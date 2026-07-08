package com.sk89q.worldedit.internal.cui;

import com.sk89q.worldedit.Vector;

public class SelectionEllipsoidPointEvent implements CUIEvent {
   protected final int id;
   protected final Vector pos;

   public SelectionEllipsoidPointEvent(int id, Vector pos) {
      this.id = id;
      this.pos = pos;
   }

   @Override
   public String getTypeId() {
      return "e";
   }

   @Override
   public String[] getParameters() {
      return new String[]{
         String.valueOf(this.id), String.valueOf(this.pos.getBlockX()), String.valueOf(this.pos.getBlockY()), String.valueOf(this.pos.getBlockZ())
      };
   }
}
