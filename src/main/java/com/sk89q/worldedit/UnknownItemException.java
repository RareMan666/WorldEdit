package com.sk89q.worldedit;

public class UnknownItemException extends WorldEditException {
   private String type;

   public UnknownItemException(String type) {
      this.type = type;
   }

   public String getID() {
      return this.type;
   }
}
