package com.sk89q.worldedit.world.storage;

import javax.annotation.Nullable;

public class MissingWorldException extends ChunkStoreException {
   private String worldName;

   public MissingWorldException() {
   }

   public MissingWorldException(String worldName) {
      this.worldName = worldName;
   }

   public MissingWorldException(String msg, String worldName) {
      super(msg);
      this.worldName = worldName;
   }

   @Nullable
   public String getWorldName() {
      return this.worldName;
   }
}
