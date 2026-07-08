package com.sk89q.worldedit.extent.clipboard.io;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.world.registry.WorldData;
import java.io.Closeable;
import java.io.IOException;

public interface ClipboardWriter extends Closeable {
   void write(Clipboard var1, WorldData var2) throws IOException;
}
