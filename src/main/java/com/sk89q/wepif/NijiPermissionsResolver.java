package com.sk89q.wepif;

import com.nijikokun.bukkit.Permissions.Permissions;
import com.sk89q.util.yaml.YAMLProcessor;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class NijiPermissionsResolver implements PermissionsResolver {
   private static final Logger log = Logger.getLogger(NijiPermissionsResolver.class.getCanonicalName());
   private Server server;
   private Permissions api;

   public static PermissionsResolver factory(Server server, YAMLProcessor config) {
      PluginManager pluginManager = server.getPluginManager();

      try {
         Class.forName("com.nijikokun.bukkit.Permissions.Permissions");
      } catch (ClassNotFoundException var4) {
         return null;
      }

      Plugin plugin = pluginManager.getPlugin("Permissions");
      if (plugin == null || !(plugin instanceof Permissions)) {
         return null;
      } else {
         return config.getBoolean("ignore-nijiperms-bridges", true) && isFakeNijiPerms(plugin)
            ? null
            : new NijiPermissionsResolver(server, (Permissions)plugin);
      }
   }

   @Override
   public void load() {
   }

   public NijiPermissionsResolver(Server server, Permissions plugin) {
      this.server = server;
      this.api = plugin;
   }

   @Override
   public boolean hasPermission(String name, String permission) {
      try {
         Player player = this.server.getPlayerExact(name);
         if (player == null) {
            return false;
         } else {
            try {
               return this.api.getHandler().has(player, permission);
            } catch (Throwable var5) {
               return Permissions.Security.permission(player, permission);
            }
         }
      } catch (Throwable var6) {
         log.log(Level.WARNING, "Failed to check permissions", var6);
         return false;
      }
   }

   @Override
   public boolean hasPermission(String worldName, String name, String permission) {
      try {
         try {
            return this.api.getHandler().has(worldName, name, permission);
         } catch (Throwable var5) {
            return this.api.getHandler().has(this.server.getPlayerExact(name), permission);
         }
      } catch (Throwable var6) {
         log.log(Level.WARNING, "Failed to check permissions", var6);
         return false;
      }
   }

   @Override
   public boolean inGroup(String name, String group) {
      try {
         Player player = this.server.getPlayerExact(name);
         if (player == null) {
            return false;
         } else {
            try {
               return this.api.getHandler().inGroup(player.getWorld().getName(), name, group);
            } catch (Throwable var5) {
               return Permissions.Security.inGroup(name, group);
            }
         }
      } catch (Throwable var6) {
         log.log(Level.WARNING, "Failed to check groups", var6);
         return false;
      }
   }

   @Override
   public String[] getGroups(String name) {
      try {
         Player player = this.server.getPlayerExact(name);
         if (player == null) {
            return new String[0];
         } else {
            String[] groups = null;

            try {
               groups = this.api.getHandler().getGroups(player.getWorld().getName(), player.getName());
            } catch (Throwable var6) {
               String group = Permissions.Security.getGroup(player.getWorld().getName(), player.getName());
               if (group != null) {
                  groups = new String[]{group};
               }
            }

            return groups == null ? new String[0] : groups;
         }
      } catch (Throwable var7) {
         log.log(Level.WARNING, "Failed to get groups", var7);
         return new String[0];
      }
   }

   @Override
   public boolean hasPermission(OfflinePlayer player, String permission) {
      return this.hasPermission(player.getName(), permission);
   }

   @Override
   public boolean hasPermission(String worldName, OfflinePlayer player, String permission) {
      return this.hasPermission(worldName, player.getName(), permission);
   }

   @Override
   public boolean inGroup(OfflinePlayer player, String group) {
      return this.inGroup(player.getName(), group);
   }

   @Override
   public String[] getGroups(OfflinePlayer player) {
      return this.getGroups(player.getName());
   }

   public static boolean isFakeNijiPerms(Plugin plugin) {
      PluginCommand permsCommand = Bukkit.getServer().getPluginCommand("permissions");
      return permsCommand == null || !permsCommand.getPlugin().equals(plugin);
   }

   @Override
   public String getDetectionMessage() {
      return "Permissions plugin detected! Using Permissions plugin for permissions.";
   }
}
