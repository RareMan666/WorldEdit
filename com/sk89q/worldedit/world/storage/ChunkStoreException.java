package com.sk89q.worldedit.world.storage;

import com.sk89q.worldedit.world.DataException;

public class ChunkStoreException extends DataException {
   public ChunkStoreException(String msg) {
      super(msg);
   }

   public ChunkStoreException() {
   }
}
