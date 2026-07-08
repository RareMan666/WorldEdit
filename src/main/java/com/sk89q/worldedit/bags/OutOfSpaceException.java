package com.sk89q.worldedit.bags;

@Deprecated
public class OutOfSpaceException extends BlockBagException {
   private int id;

   public OutOfSpaceException(int id) {
      this.id = id;
   }

   public int getID() {
      return this.id;
   }
}
