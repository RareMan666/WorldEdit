package com.sk89q.worldedit.util.io.file;

public class FileSelectionAbortedException extends FilenameException {
   public FileSelectionAbortedException() {
      super("");
   }

   public FileSelectionAbortedException(String msg) {
      super("", msg);
   }
}
