package com.sk89q.worldedit.util.io.file;

public class FilenameResolutionException extends FilenameException {
   public FilenameResolutionException(String filename) {
      super(filename);
   }

   public FilenameResolutionException(String filename, String msg) {
      super(filename, msg);
   }
}
