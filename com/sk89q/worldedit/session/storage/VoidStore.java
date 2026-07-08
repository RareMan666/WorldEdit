package com.sk89q.worldedit.session.storage;

import com.sk89q.worldedit.LocalSession;
import java.io.IOException;
import java.util.UUID;

public class VoidStore implements SessionStore {
   @Override
   public LocalSession load(UUID id) throws IOException {
      return new LocalSession();
   }

   @Override
   public void save(UUID id, LocalSession session) throws IOException {
   }
}
