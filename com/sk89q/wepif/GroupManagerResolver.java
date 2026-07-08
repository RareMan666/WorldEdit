package com.sk89q.wepif;

import com.sk89q.util.yaml.YAMLProcessor;
import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.anjocaido.groupmanager.permissions.AnjoPermissionsHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.permissions.Permissible;

public class GroupManagerResolver extends DinnerPermsResolver {
   private final WorldsHolder worldsHolder;

   public static PermissionsResolver factory(Server server, YAMLProcessor config) {
      try {
         WorldsHolder worldsHolder = (WorldsHolder)server.getServicesManager().load(WorldsHolder.class);
         return worldsHolder == null ? null : new GroupManagerResolver(server, worldsHolder);
      } catch (Throwable var3) {
         return null;
      }
   }

   public GroupManagerResolver(Server server, WorldsHolder worldsHolder) {
      super(server);
      this.worldsHolder = worldsHolder;
   }

   @Override
   public void load() {
   }

   private boolean nameNotSafe(String perm) {
      return perm == null || perm.isEmpty();
   }

   private AnjoPermissionsHandler getPermissionHandler(World world) {
      return world != null ? this.worldsHolder.getWorldPermissions(world.getName()) : this.worldsHolder.getDefaultWorld().getPermissionsHandler();
   }

   @Override
   public String[] getGroups(String name) {
      AnjoPermissionsHandler permissionHandler = this.getPermissionHandler(null);
      return permissionHandler == null ? new String[0] : permissionHandler.getGroups(name);
   }

   @Override
   public boolean hasPermission(OfflinePlayer player, String permission) {
      if (this.nameNotSafe(permission)) {
         return false;
      } else {
         Permissible permissible = this.getPermissible(player);
         return permissible == null
            ? this.getPermissionHandler(player.getPlayer().getWorld()).permission(player.getName(), permission)
            : permissible.hasPermission(permission);
      }
   }

   @Override
   public boolean hasPermission(String worldName, OfflinePlayer player, String permission) {
      if (this.nameNotSafe(permission)) {
         return false;
      } else {
         String name = player.getName();
         World world = worldName != null ? this.server.getWorld(worldName) : player.getPlayer().getWorld();
         AnjoPermissionsHandler permissionHandler = this.getPermissionHandler(world);
         return permissionHandler != null && permissionHandler.permission(name, permission);
      }
   }

   @Override
   public boolean inGroup(OfflinePlayer player, String group) {
      if (super.inGroup(player, group)) {
         return true;
      } else if (this.nameNotSafe(group)) {
         return false;
      } else {
         AnjoPermissionsHandler permissionHandler = this.getPermissionHandler(null);
         return permissionHandler != null && permissionHandler.inGroup(player.getName(), group);
      }
   }

   @Override
   public String[] getGroups(OfflinePlayer player) {
      return this.getGroups(player.getName());
   }

   @Override
   public String getDetectionMessage() {
      return "GroupManager detected! Using GroupManager for permissions.";
   }
}
