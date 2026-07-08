package com.sk89q.wepif;

import com.sk89q.util.yaml.YAMLProcessor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.permissions.Permissible;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.PermissionUser;

public class PermissionsExResolver extends DinnerPermsResolver {
   private final PermissionManager manager;

   public static PermissionsResolver factory(Server server, YAMLProcessor config) {
      try {
         PermissionManager manager = (PermissionManager)server.getServicesManager().load(PermissionManager.class);
         return manager == null ? null : new PermissionsExResolver(server, manager);
      } catch (Throwable var3) {
         return null;
      }
   }

   public PermissionsExResolver(Server server, PermissionManager manager) {
      super(server);
      this.manager = manager;
   }

   @Override
   public boolean hasPermission(String worldName, String name, String permission) {
      return this.manager.has(name, permission, worldName);
   }

   @Override
   public boolean hasPermission(OfflinePlayer player, String permission) {
      Permissible permissible = this.getPermissible(player);
      return permissible == null ? this.manager.has(player.getUniqueId(), permission, null) : permissible.hasPermission(permission);
   }

   @Override
   public boolean hasPermission(String worldName, OfflinePlayer player, String permission) {
      return this.manager.has(player.getUniqueId(), permission, worldName);
   }

   @Override
   public boolean inGroup(OfflinePlayer player, String group) {
      return super.inGroup(player, group) || this.manager.getUser(player.getUniqueId()).inGroup(group);
   }

   @Override
   public String[] getGroups(OfflinePlayer player) {
      if (this.getPermissible(player) == null) {
         PermissionUser user = this.manager.getUser(player.getUniqueId());
         return user == null ? new String[0] : user.getGroupsNames();
      } else {
         return super.getGroups(player);
      }
   }

   @Override
   public String getDetectionMessage() {
      return "PermissionsEx detected! Using PermissionsEx for permissions.";
   }
}
