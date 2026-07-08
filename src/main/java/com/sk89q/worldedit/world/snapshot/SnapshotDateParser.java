package com.sk89q.worldedit.world.snapshot;

import java.io.File;
import java.util.Calendar;
import javax.annotation.Nullable;

public interface SnapshotDateParser {
   @Nullable
   Calendar detectDate(File var1);
}
