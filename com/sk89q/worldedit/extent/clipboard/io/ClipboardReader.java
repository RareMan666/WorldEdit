package com.sk89q.worldedit.extent.clipboard.io;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.world.registry.WorldData;
import java.io.IOException;

public interface ClipboardReader {
   Clipboard read(WorldData var1) throws IOException;
}
