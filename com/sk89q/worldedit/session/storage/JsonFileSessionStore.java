package com.sk89q.worldedit.session.storage;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.internal.gson.Gson;
import com.sk89q.worldedit.internal.gson.GsonBuilder;
import com.sk89q.worldedit.internal.gson.JsonIOException;
import com.sk89q.worldedit.internal.gson.JsonParseException;
import com.sk89q.worldedit.util.gson.GsonUtil;
import com.sk89q.worldedit.util.io.Closer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonFileSessionStore implements SessionStore {
   private static final Logger log = Logger.getLogger(JsonFileSessionStore.class.getCanonicalName());
   private final Gson gson;
   private final File dir;

   public JsonFileSessionStore(File dir) {
      Preconditions.checkNotNull(dir);
      if (!dir.isDirectory() && !dir.mkdirs()) {
         log.log(Level.WARNING, "Failed to create directory '" + dir.getPath() + "' for sessions");
      }

      this.dir = dir;
      GsonBuilder builder = GsonUtil.createBuilder();
      this.gson = builder.create();
   }

   private File getPath(UUID id) {
      Preconditions.checkNotNull(id);
      return new File(this.dir, id + ".json");
   }

   @Override
   public LocalSession load(UUID id) throws IOException {
      File file = this.getPath(id);
      Closer closer = Closer.create();

      LocalSession br;
      try {
         FileReader fr = closer.register(new FileReader(file));
         BufferedReader brx = closer.register(new BufferedReader(fr));
         return this.gson.fromJson(brx, LocalSession.class);
      } catch (JsonParseException var17) {
         throw new IOException(var17);
      } catch (FileNotFoundException var18) {
         br = new LocalSession();
      } finally {
         try {
            closer.close();
         } catch (IOException var16) {
         }
      }

      return br;
   }

   @Override
   public void save(UUID id, LocalSession session) throws IOException {
      File finalFile = this.getPath(id);
      File tempFile = new File(finalFile.getParentFile(), finalFile.getName() + ".tmp");
      Closer closer = Closer.create();

      try {
         FileWriter fr = closer.register(new FileWriter(tempFile));
         BufferedWriter bw = closer.register(new BufferedWriter(fr));
         this.gson.toJson(session, bw);
      } catch (JsonIOException var15) {
         throw new IOException(var15);
      } finally {
         try {
            closer.close();
         } catch (IOException var14) {
         }
      }

      if (finalFile.exists() && !finalFile.delete()) {
         log.log(Level.WARNING, "Failed to delete " + finalFile.getPath() + " so the .tmp file can replace it");
      }

      if (!tempFile.renameTo(finalFile)) {
         log.log(Level.WARNING, "Failed to rename temporary session file to " + finalFile.getPath());
      }
   }
}
