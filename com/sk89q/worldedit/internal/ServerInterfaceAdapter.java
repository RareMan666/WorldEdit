package com.sk89q.worldedit.internal;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.ServerInterface;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Capability;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extension.platform.Preference;
import com.sk89q.worldedit.util.command.Dispatcher;
import com.sk89q.worldedit.world.World;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

public class ServerInterfaceAdapter extends ServerInterface {
   private final Platform platform;

   private ServerInterfaceAdapter(Platform platform) {
      Preconditions.checkNotNull(platform);
      this.platform = platform;
   }

   @Override
   public int resolveItem(String name) {
      return this.platform.resolveItem(name);
   }

   @Override
   public boolean isValidMobType(String type) {
      return this.platform.isValidMobType(type);
   }

   @Override
   public void reload() {
      this.platform.reload();
   }

   @Override
   public int schedule(long delay, long period, Runnable task) {
      return this.platform.schedule(delay, period, task);
   }

   @Override
   public List<? extends World> getWorlds() {
      return this.platform.getWorlds();
   }

   @Nullable
   @Override
   public Player matchPlayer(Player player) {
      return this.platform.matchPlayer(player);
   }

   @Nullable
   @Override
   public World matchWorld(World world) {
      return this.platform.matchWorld(world);
   }

   @Override
   public void registerCommands(Dispatcher dispatcher) {
      this.platform.registerCommands(dispatcher);
   }

   @Override
   public void registerGameHooks() {
   }

   @Override
   public LocalConfiguration getConfiguration() {
      return this.platform.getConfiguration();
   }

   @Override
   public String getVersion() {
      return this.platform.getVersion();
   }

   @Override
   public String getPlatformName() {
      return this.platform.getPlatformName();
   }

   @Override
   public String getPlatformVersion() {
      return this.platform.getPlatformVersion();
   }

   @Override
   public Map<Capability, Preference> getCapabilities() {
      return this.platform.getCapabilities();
   }

   public static ServerInterface adapt(Platform platform) {
      return new ServerInterfaceAdapter(platform);
   }
}
