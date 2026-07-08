package com.sk89q.wepif;

import com.sk89q.util.yaml.YAMLProcessor;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class DinnerPermsResolver implements PermissionsResolver {
   public static final String GROUP_PREFIX = "group.";
   protected final Server server;

   public DinnerPermsResolver(Server server) {
      this.server = server;
   }

   public static PermissionsResolver factory(Server server, YAMLProcessor config) {
      return new DinnerPermsResolver(server);
   }

   @Override
   public void load() {
   }

   @Override
   public boolean hasPermission(String name, String permission) {
      return this.hasPermission(this.server.getOfflinePlayer(name), permission);
   }

   @Override
   public boolean hasPermission(String worldName, String name, String permission) {
      return this.hasPermission(worldName, this.server.getOfflinePlayer(name), permission);
   }

   @Override
   public boolean inGroup(String name, String group) {
      return this.inGroup(this.server.getOfflinePlayer(name), group);
   }

   @Override
   public String[] getGroups(String name) {
      return this.getGroups(this.server.getOfflinePlayer(name));
   }

   @Override
   public boolean hasPermission(OfflinePlayer player, String permission) {
      Permissible perms = this.getPermissible(player);
      if (perms == null) {
         return false;
      } else {
         switch (this.internalHasPermission(perms, permission)) {
            case -1:
               return false;
            case 1:
               return true;
            default:
               for (int dotPos = permission.lastIndexOf("."); dotPos > -1; dotPos = permission.lastIndexOf(".", dotPos - 1)) {
                  switch (this.internalHasPermission(perms, permission.substring(0, dotPos + 1) + "*")) {
                     case -1:
                        return false;
                     case 1:
                        return true;
                  }
               }

               return this.internalHasPermission(perms, "*") == 1;
         }
      }
   }

   @Override
   public boolean hasPermission(String worldName, OfflinePlayer player, String permission) {
      return this.hasPermission(player, permission);
   }

   @Override
   public boolean inGroup(OfflinePlayer player, String group) {
      Permissible perms = this.getPermissible(player);
      if (perms == null) {
         return false;
      } else {
         String perm = "group." + group;
         return perms.isPermissionSet(perm) && perms.hasPermission(perm);
      }
   }

   @Override
   public String[] getGroups(OfflinePlayer player) {
      Permissible perms = this.getPermissible(player);
      if (perms == null) {
         return new String[0];
      } else {
         List<String> groupNames = new ArrayList<>();

         for (PermissionAttachmentInfo permAttach : perms.getEffectivePermissions()) {
            String perm = permAttach.getPermission();
            if (perm.startsWith("group.") && permAttach.getValue()) {
               groupNames.add(perm.substring("group.".length(), perm.length()));
            }
         }

         return groupNames.toArray(new String[groupNames.size()]);
      }
   }

   public Permissible getPermissible(OfflinePlayer offline) {
      if (offline == null) {
         return null;
      } else {
         Permissible perm = null;
         if (offline instanceof Permissible) {
            perm = (Permissible)offline;
         } else {
            Player player = offline.getPlayer();
            if (player != null) {
               perm = player;
            }
         }

         return perm;
      }
   }

   public int internalHasPermission(Permissible perms, String permission) {
      if (perms.isPermissionSet(permission)) {
         return perms.hasPermission(permission) ? 1 : -1;
      } else {
         Permission perm = this.server.getPluginManager().getPermission(permission);
         if (perm != null) {
            return perm.getDefault().getValue(perms.isOp()) ? 1 : 0;
         } else {
            return 0;
         }
      }
   }

   @Override
   public String getDetectionMessage() {
      return "Using the Bukkit Permissions API.";
   }
}
