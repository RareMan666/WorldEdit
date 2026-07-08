package com.sk89q.worldedit.util.io.file;

public class InvalidFilenameException extends FilenameException {
   public InvalidFilenameException(String filename) {
      super(filename);
   }

   public InvalidFilenameException(String filename, String msg) {
      super(filename, msg);
   }
}
