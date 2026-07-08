package com.sk89q.worldedit.world.storage;

import com.sk89q.worldedit.Vector2D;

public class MissingChunkException extends ChunkStoreException {
   private Vector2D position;

   public MissingChunkException() {
   }

   public MissingChunkException(Vector2D position) {
      this.position = position;
   }

   public Vector2D getChunkPosition() {
      return this.position;
   }
}
