package com.sk89q.worldedit.util.io.file;

import com.sk89q.worldedit.WorldEditException;

public class FilenameException extends WorldEditException {
   private String filename;

   public FilenameException(String filename) {
      this.filename = filename;
   }

   public FilenameException(String filename, String msg) {
      super(msg);
      this.filename = filename;
   }

   public String getFilename() {
      return this.filename;
   }
}
