package com.sk89q.worldedit.extension.platform;

import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.util.command.Dispatcher;
import com.sk89q.worldedit.world.World;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public interface Platform {
   int resolveItem(String var1);

   boolean isValidMobType(String var1);

   void reload();

   int schedule(long var1, long var3, Runnable var5);

   List<? extends World> getWorlds();

   @Nullable
   Player matchPlayer(Player var1);

   @Nullable
   World matchWorld(World var1);

   void registerCommands(Dispatcher var1);

   void registerGameHooks();

   LocalConfiguration getConfiguration();

   String getVersion();

   String getPlatformName();

   String getPlatformVersion();

   Map<Capability, Preference> getCapabilities();
}
