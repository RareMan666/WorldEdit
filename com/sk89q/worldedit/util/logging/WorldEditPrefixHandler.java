package com.sk89q.worldedit.util.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public final class WorldEditPrefixHandler extends Handler {
   private WorldEditPrefixHandler() {
   }

   @Override
   public void publish(LogRecord record) {
      String message = record.getMessage();
      if (!message.startsWith("WorldEdit: ") && !message.startsWith("[WorldEdit] ")) {
         record.setMessage("[WorldEdit] " + message);
      }
   }

   @Override
   public void flush() {
   }

   @Override
   public void close() throws SecurityException {
   }

   public static void register(String name) {
      Logger.getLogger(name).addHandler(new WorldEditPrefixHandler());
   }
}
