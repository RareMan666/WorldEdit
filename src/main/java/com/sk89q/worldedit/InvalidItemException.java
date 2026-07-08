package com.sk89q.worldedit;

public class InvalidItemException extends DisallowedItemException {
   public InvalidItemException(String type, String message) {
      super(type, message);
   }
}
