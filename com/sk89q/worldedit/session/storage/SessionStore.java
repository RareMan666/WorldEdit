package com.sk89q.worldedit.session.storage;

import com.sk89q.worldedit.LocalSession;
import java.io.IOException;
import java.util.UUID;

public interface SessionStore {
   LocalSession load(UUID var1) throws IOException;

   void save(UUID var1, LocalSession var2) throws IOException;
}
