package com.sk89q.worldedit.command.tool;

import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.util.Location;

public interface BlockTool extends Tool {
   boolean actPrimary(Platform var1, LocalConfiguration var2, Player var3, LocalSession var4, Location var5);
}
