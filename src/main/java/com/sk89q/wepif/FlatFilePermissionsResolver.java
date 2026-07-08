package com.sk89q.wepif;

import com.sk89q.util.yaml.YAMLProcessor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;

public class FlatFilePermissionsResolver implements PermissionsResolver {
   private static final Logger log = Logger.getLogger(FlatFilePermissionsResolver.class.getCanonicalName());
   private Map<String, Set<String>> userPermissionsCache;
   private Set<String> defaultPermissionsCache;
   private Map<String, Set<String>> userGroups;
   private final File groupFile;
   private final File userFile;

   public static PermissionsResolver factory(Server server, YAMLProcessor config) {
      File groups = new File("perms_groups.txt");
      File users = new File("perms_users.txt");
      return groups.exists() && users.exists() ? new FlatFilePermissionsResolver(groups, users) : null;
   }

   public FlatFilePermissionsResolver() {
      this(new File("perms_groups.txt"), new File("perms_users.txt"));
   }

   public FlatFilePermissionsResolver(File groupFile, File userFile) {
      this.groupFile = groupFile;
      this.userFile = userFile;
   }

   @Deprecated
   public static boolean filesExists() {
      return new File("perms_groups.txt").exists() && new File("perms_users.txt").exists();
   }

   public Map<String, Set<String>> loadGroupPermissions() {
      Map<String, Set<String>> userGroupPermissions = new HashMap<>();
      BufferedReader buff = null;

      try {
         FileReader input = new FileReader(this.groupFile);
         buff = new BufferedReader(input);

         String line;
         while ((line = buff.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty() && line.charAt(0) != ';' && line.charAt(0) != '#') {
               String[] parts = line.split(":");
               String key = parts[0];
               if (parts.length > 1) {
                  String[] perms = parts[1].split(",");
                  Set<String> groupPerms = new HashSet<>(Arrays.asList(perms));
                  userGroupPermissions.put(key, groupPerms);
               }
            }
         }
      } catch (IOException var17) {
         log.log(Level.WARNING, "Failed to load permissions", (Throwable)var17);
      } finally {
         try {
            if (buff != null) {
               buff.close();
            }
         } catch (IOException var16) {
         }
      }

      return userGroupPermissions;
   }

   @Override
   public void load() {
      this.userGroups = new HashMap<>();
      this.userPermissionsCache = new HashMap<>();
      this.defaultPermissionsCache = new HashSet<>();
      Map<String, Set<String>> userGroupPermissions = this.loadGroupPermissions();
      if (userGroupPermissions.containsKey("default")) {
         this.defaultPermissionsCache = userGroupPermissions.get("default");
      }

      BufferedReader buff = null;

      try {
         FileReader input = new FileReader(this.userFile);
         buff = new BufferedReader(input);

         String line;
         while ((line = buff.readLine()) != null) {
            Set<String> permsCache = new HashSet<>();
            line = line.trim();
            if (!line.isEmpty() && line.charAt(0) != ';' && line.charAt(0) != '#') {
               String[] parts = line.split(":");
               String key = parts[0];
               if (parts.length > 1) {
                  String[] groups = (parts[1] + ",default").split(",");
                  String[] perms = parts.length > 2 ? parts[2].split(",") : new String[0];
                  permsCache.addAll(Arrays.asList(perms));

                  for (String group : groups) {
                     Set<String> groupPerms = userGroupPermissions.get(group);
                     if (groupPerms != null) {
                        permsCache.addAll(groupPerms);
                     }
                  }

                  this.userPermissionsCache.put(key.toLowerCase(), permsCache);
                  this.userGroups.put(key.toLowerCase(), new HashSet<>(Arrays.asList(groups)));
               }
            }
         }
      } catch (IOException var23) {
         log.log(Level.WARNING, "Failed to load permissions", (Throwable)var23);
      } finally {
         try {
            if (buff != null) {
               buff.close();
            }
         } catch (IOException var22) {
         }
      }
   }

   @Override
   public boolean hasPermission(String player, String permission) {
      int dotPos = permission.lastIndexOf(".");
      if (dotPos > -1 && this.hasPermission(player, permission.substring(0, dotPos))) {
         return true;
      } else {
         Set<String> perms = this.userPermissionsCache.get(player.toLowerCase());
         return perms == null
            ? this.defaultPermissionsCache.contains(permission) || this.defaultPermissionsCache.contains("*")
            : perms.contains("*") || perms.contains(permission);
      }
   }

   @Override
   public boolean hasPermission(String worldName, String player, String permission) {
      return this.hasPermission(player, "worlds." + worldName + "." + permission) || this.hasPermission(player, permission);
   }

   @Override
   public boolean inGroup(String player, String group) {
      Set<String> groups = this.userGroups.get(player.toLowerCase());
      return groups != null && groups.contains(group);
   }

   @Override
   public String[] getGroups(String player) {
      Set<String> groups = this.userGroups.get(player.toLowerCase());
      return groups == null ? new String[0] : groups.toArray(new String[groups.size()]);
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

   @Override
   public String getDetectionMessage() {
      return "perms_groups.txt and perms_users.txt detected! Using flat file permissions.";
   }
}
