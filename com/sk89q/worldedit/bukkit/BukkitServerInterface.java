package com.sk89q.worldedit.bukkit;

import com.sk89q.bukkit.util.CommandInfo;
import com.sk89q.bukkit.util.CommandRegistration;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.ServerInterface;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Capability;
import com.sk89q.worldedit.extension.platform.MultiUserPlatform;
import com.sk89q.worldedit.extension.platform.Preference;
import com.sk89q.worldedit.util.command.CommandMapping;
import com.sk89q.worldedit.util.command.Description;
import com.sk89q.worldedit.util.command.Dispatcher;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

public class BukkitServerInterface extends ServerInterface implements MultiUserPlatform {
   public Server server;
   public WorldEditPlugin plugin;
   private CommandRegistration dynamicCommands;
   private BukkitBiomeRegistry biomes;
   private boolean hookingEvents;

   public BukkitServerInterface(WorldEditPlugin plugin, Server server) {
      this.plugin = plugin;
      this.server = server;
      this.biomes = new BukkitBiomeRegistry();
      this.dynamicCommands = new CommandRegistration(plugin);
   }

   boolean isHookingEvents() {
      return this.hookingEvents;
   }

   @Override
   public int resolveItem(String name) {
      Material mat = Material.matchMaterial(name);
      return mat == null ? 0 : mat.getId();
   }

   @Override
   public boolean isValidMobType(String type) {
      EntityType entityType = EntityType.fromName(type);
      return entityType != null && entityType.isAlive();
   }

   @Override
   public void reload() {
      this.plugin.loadConfiguration();
   }

   @Override
   public int schedule(long delay, long period, Runnable task) {
      return Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, task, delay, period);
   }

   @Override
   public List<LocalWorld> getWorlds() {
      List<World> worlds = this.server.getWorlds();
      List<LocalWorld> ret = new ArrayList<>(worlds.size());

      for (World world : worlds) {
         ret.add(BukkitUtil.getLocalWorld(world));
      }

      return ret;
   }

   @Nullable
   @Override
   public Player matchPlayer(Player player) {
      if (player instanceof BukkitPlayer) {
         return player;
      } else {
         org.bukkit.entity.Player bukkitPlayer = this.server.getPlayerExact(player.getName());
         return bukkitPlayer != null ? new BukkitPlayer(this.plugin, this, bukkitPlayer) : null;
      }
   }

   @Nullable
   public BukkitWorld matchWorld(com.sk89q.worldedit.world.World world) {
      if (world instanceof BukkitWorld) {
         return (BukkitWorld)world;
      } else {
         World bukkitWorld = this.server.getWorld(world.getName());
         return bukkitWorld != null ? new BukkitWorld(bukkitWorld) : null;
      }
   }

   @Override
   public void registerCommands(Dispatcher dispatcher) {
      List<CommandInfo> toRegister = new ArrayList<>();
      BukkitCommandInspector inspector = new BukkitCommandInspector(this.plugin, dispatcher);

      for (CommandMapping command : dispatcher.getCommands()) {
         Description description = command.getDescription();
         List<String> permissions = description.getPermissions();
         String[] permissionsArray = new String[permissions.size()];
         permissions.toArray(permissionsArray);
         toRegister.add(new CommandInfo(description.getUsage(), description.getDescription(), command.getAllAliases(), inspector, permissionsArray));
      }

      this.dynamicCommands.register(toRegister);
   }

   @Override
   public void registerGameHooks() {
      this.hookingEvents = true;
   }

   @Override
   public LocalConfiguration getConfiguration() {
      return this.plugin.getLocalConfiguration();
   }

   @Override
   public String getVersion() {
      return this.plugin.getDescription().getVersion();
   }

   @Override
   public String getPlatformName() {
      return "Bukkit-Official";
   }

   @Override
   public String getPlatformVersion() {
      return this.plugin.getDescription().getVersion();
   }

   @Override
   public Map<Capability, Preference> getCapabilities() {
      Map<Capability, Preference> capabilities = new EnumMap<>(Capability.class);
      capabilities.put(Capability.CONFIGURATION, Preference.NORMAL);
      capabilities.put(Capability.WORLDEDIT_CUI, Preference.NORMAL);
      capabilities.put(Capability.GAME_HOOKS, Preference.PREFERRED);
      capabilities.put(Capability.PERMISSIONS, Preference.PREFERRED);
      capabilities.put(Capability.USER_COMMANDS, Preference.PREFERRED);
      capabilities.put(Capability.WORLD_EDITING, Preference.PREFER_OTHERS);
      return capabilities;
   }

   public void unregisterCommands() {
      this.dynamicCommands.unregisterCommands();
   }

   @Override
   public Collection<Actor> getConnectedUsers() {
      List<Actor> users = new ArrayList<>();

      for (org.bukkit.entity.Player player : Bukkit.getServer().getOnlinePlayers()) {
         users.add(new BukkitPlayer(this.plugin, this, player));
      }

      return users;
   }
}
