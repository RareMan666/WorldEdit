package com.sk89q.worldedit;

public class NotABlockException extends WorldEditException {
   public NotABlockException() {
      super("This item is not a block.");
   }

   public NotABlockException(String input) {
      super("The item '" + input + "' is not a block.");
   }

   public NotABlockException(int input) {
      super("The item with the ID " + input + " is not a block.");
   }
}
