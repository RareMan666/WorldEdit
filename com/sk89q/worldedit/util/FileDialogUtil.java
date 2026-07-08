package com.sk89q.worldedit.util;

import com.sk89q.util.StringUtil;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public final class FileDialogUtil {
   private FileDialogUtil() {
   }

   public static File showSaveDialog(String[] exts) {
      JFileChooser dialog = new JFileChooser();
      if (exts != null) {
         dialog.setFileFilter(new FileDialogUtil.ExtensionFilter(exts));
      }

      int returnVal = dialog.showSaveDialog(null);
      return returnVal == 0 ? dialog.getSelectedFile() : null;
   }

   public static File showOpenDialog(String[] exts) {
      JFileChooser dialog = new JFileChooser();
      if (exts != null) {
         dialog.setFileFilter(new FileDialogUtil.ExtensionFilter(exts));
      }

      int returnVal = dialog.showOpenDialog(null);
      return returnVal == 0 ? dialog.getSelectedFile() : null;
   }

   private static class ExtensionFilter extends FileFilter {
      private Set<String> exts;
      private String desc;

      private ExtensionFilter(String[] exts) {
         this.exts = new HashSet<>(Arrays.asList(exts));
         this.desc = StringUtil.joinString(exts, ",");
      }

      @Override
      public boolean accept(File f) {
         if (f.isDirectory()) {
            return true;
         } else {
            String path = f.getPath();
            int index = path.lastIndexOf(46);
            return index != -1 && index != path.length() - 1 ? this.exts.contains(path.indexOf(index + 1)) : false;
         }
      }

      @Override
      public String getDescription() {
         return this.desc;
      }
   }
}
