package com.sk89q.worldedit.world.snapshot;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ModificationTimerParser implements SnapshotDateParser {
   @Override
   public Calendar detectDate(File file) {
      Calendar cal = new GregorianCalendar();
      cal.setTimeInMillis(file.lastModified());
      return cal;
   }
}
