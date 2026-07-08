package com.sk89q.worldedit.util;

import com.google.common.base.Joiner;
import com.sk89q.worldedit.WorldEdit;

public final class Java8Detector {
   public static void notifyIfNot8() {
      String[] ver = System.getProperty("java.version").split("\\.");
      int major = Integer.parseInt(ver[1]);
      if (major <= 7) {
         WorldEdit.logger.warning("WorldEdit has detected you are using Java 7 (based on detected version " + Joiner.on('.').join(ver) + ").");
         WorldEdit.logger
            .warning(
               "WorldEdit will stop supporting Java less than version 8 in the future, due to Java 7 being EOL since April 2015. Please update your server to Java 8."
            );
      }
   }

   private Java8Detector() {
   }
}
