package com.sk89q.worldedit.bukkit.adapter;

public class AdapterLoadException extends Exception {
   public AdapterLoadException() {
   }

   public AdapterLoadException(String message) {
      super(message);
   }

   public AdapterLoadException(String message, Throwable cause) {
      super(message, cause);
   }

   public AdapterLoadException(Throwable cause) {
      super(cause);
   }
}
