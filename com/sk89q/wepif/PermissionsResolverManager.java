package com.sk89q.wepif;

import com.sk89q.util.yaml.YAMLFormat;
import com.sk89q.util.yaml.YAMLProcessor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class PermissionsResolverManager implements PermissionsResolver {
   private static final String CONFIG_HEADER = "#\r\n# WEPIF Configuration File\r\n#\r\n# This file handles permissions configuration for every plugin using WEPIF\r\n#\r\n# About editing this file:\r\n# - DO NOT USE TABS. You MUST use spaces or Bukkit will complain. If\r\n#   you use an editor like Notepad++ (recommended for Windows users), you\r\n#   must configure it to \"replace tabs with spaces.\" In Notepad++, this can\r\n#   be changed in Settings > Preferences > Language Menu.\r\n# - Don't get rid of the indents. They are indented so some entries are\r\n#   in categories (like \"enforce-single-session\" is in the \"protection\"\r\n#   category.\r\n# - If you want to check the format of this file before putting it\r\n#   into WEPIF, paste it into http://yaml-online-parser.appspot.com/\r\n#   and see if it gives \"ERROR:\".\r\n# - Lines starting with # are comments and so they are ignored.\r\n#\r\n# About Configuration Permissions\r\n# - See http://wiki.sk89q.com/wiki/WorldEdit/Permissions/Bukkit\r\n# - Now with multiworld support (see example)\r\n\r\n";
   private static PermissionsResolverManager instance;
   private Server server;
   private PermissionsResolver permissionResolver;
   private YAMLProcessor config;
   private Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
   private List<Class<? extends PermissionsResolver>> enabledResolvers = new ArrayList<>();
   protected Class<? extends PermissionsResolver>[] availableResolvers = new Class[]{
      PluginPermissionsResolver.class,
      // The following four resolvers depend on third-party permissions plugins
      // (PermissionsEx, bPermissions, GroupManager, Permissions/Niji) whose
      // artifacts are not in any public Maven repo. They are excluded from
      // this build; if needed, install the plugin jars to the local repo and
      // re-enable them here.
      // PermissionsExResolver.class,
      // bPermissionsResolver.class,
      // GroupManagerResolver.class,
      // NijiPermissionsResolver.class,
      DinnerPermsResolver.class,
      FlatFilePermissionsResolver.class
   };

   public static void initialize(Plugin plugin) {
      if (!isInitialized()) {
         instance = new PermissionsResolverManager(plugin);
      }
   }

   public static boolean isInitialized() {
      return instance != null;
   }

   public static PermissionsResolverManager getInstance() {
      if (!isInitialized()) {
         throw new WEPIFRuntimeException("WEPIF has not yet been initialized!");
      } else {
         return instance;
      }
   }

   protected PermissionsResolverManager(Plugin plugin) {
      this.server = plugin.getServer();
      new PermissionsResolverManager.ServerListener().register(plugin);
      this.loadConfig(new File("wepif.yml"));
      this.findResolver();
   }

   public void findResolver() {
      for (Class<? extends PermissionsResolver> resolverClass : this.enabledResolvers) {
         try {
            Method factoryMethod = resolverClass.getMethod("factory", Server.class, YAMLProcessor.class);
            this.permissionResolver = (PermissionsResolver)factoryMethod.invoke(null, this.server, this.config);
            if (this.permissionResolver != null) {
               break;
            }
         } catch (Throwable var4) {
            this.logger.log(Level.WARNING, "Error in factory method for " + resolverClass.getSimpleName(), var4);
         }
      }

      if (this.permissionResolver == null) {
         this.permissionResolver = new ConfigurationPermissionsResolver(this.config);
      }

      this.permissionResolver.load();
      this.logger.info("WEPIF: " + this.permissionResolver.getDetectionMessage());
   }

   public void setPluginPermissionsResolver(Plugin plugin) {
      if (plugin instanceof PermissionsProvider) {
         this.permissionResolver = new PluginPermissionsResolver((PermissionsProvider)plugin, plugin);
         this.logger.info("WEPIF: " + this.permissionResolver.getDetectionMessage());
      }
   }

   @Override
   public void load() {
      this.findResolver();
   }

   @Override
   public boolean hasPermission(String name, String permission) {
      return this.permissionResolver.hasPermission(name, permission);
   }

   @Override
   public boolean hasPermission(String worldName, String name, String permission) {
      return this.permissionResolver.hasPermission(worldName, name, permission);
   }

   @Override
   public boolean inGroup(String player, String group) {
      return this.permissionResolver.inGroup(player, group);
   }

   @Override
   public String[] getGroups(String player) {
      return this.permissionResolver.getGroups(player);
   }

   @Override
   public boolean hasPermission(OfflinePlayer player, String permission) {
      return this.permissionResolver.hasPermission(player, permission);
   }

   @Override
   public boolean hasPermission(String worldName, OfflinePlayer player, String permission) {
      return this.permissionResolver.hasPermission(worldName, player, permission);
   }

   @Override
   public boolean inGroup(OfflinePlayer player, String group) {
      return this.permissionResolver.inGroup(player, group);
   }

   @Override
   public String[] getGroups(OfflinePlayer player) {
      return this.permissionResolver.getGroups(player);
   }

   @Override
   public String getDetectionMessage() {
      return "Using WEPIF for permissions";
   }

   private boolean loadConfig(File file) {
      boolean isUpdated = false;
      if (!file.exists()) {
         try {
            file.createNewFile();
         } catch (IOException var12) {
            this.logger.log(Level.WARNING, "Failed to create new configuration file", (Throwable)var12);
         }
      }

      this.config = new YAMLProcessor(file, false, YAMLFormat.EXTENDED);

      try {
         this.config.load();
      } catch (IOException var11) {
         this.logger.log(Level.WARNING, "Error loading WEPIF configuration", (Throwable)var11);
      }

      List<String> keys = this.config.getKeys(null);
      this.config
         .setHeader(
            "#\r\n# WEPIF Configuration File\r\n#\r\n# This file handles permissions configuration for every plugin using WEPIF\r\n#\r\n# About editing this file:\r\n# - DO NOT USE TABS. You MUST use spaces or Bukkit will complain. If\r\n#   you use an editor like Notepad++ (recommended for Windows users), you\r\n#   must configure it to \"replace tabs with spaces.\" In Notepad++, this can\r\n#   be changed in Settings > Preferences > Language Menu.\r\n# - Don't get rid of the indents. They are indented so some entries are\r\n#   in categories (like \"enforce-single-session\" is in the \"protection\"\r\n#   category.\r\n# - If you want to check the format of this file before putting it\r\n#   into WEPIF, paste it into http://yaml-online-parser.appspot.com/\r\n#   and see if it gives \"ERROR:\".\r\n# - Lines starting with # are comments and so they are ignored.\r\n#\r\n# About Configuration Permissions\r\n# - See http://wiki.sk89q.com/wiki/WorldEdit/Permissions/Bukkit\r\n# - Now with multiworld support (see example)\r\n\r\n"
         );
      if (!keys.contains("ignore-nijiperms-bridges")) {
         this.config.setProperty("ignore-nijiperms-bridges", true);
         isUpdated = true;
      }

      if (!keys.contains("resolvers")) {
         List<String> resolvers = new ArrayList<>();

         for (Class<?> clazz : this.availableResolvers) {
            resolvers.add(clazz.getSimpleName());
         }

         this.enabledResolvers.addAll(Arrays.asList(this.availableResolvers));
         this.config.setProperty("resolvers.enabled", resolvers);
         isUpdated = true;
      } else {
         List<String> disabledResolvers = this.config.getStringList("resolvers.disabled", new ArrayList<>());
         List<String> stagedEnabled = this.config.getStringList("resolvers.enabled", null);
         Iterator<String> i = stagedEnabled.iterator();

         while (i.hasNext()) {
            String nextName = i.next();
            Class<?> next = null;

            try {
               next = Class.forName(this.getClass().getPackage().getName() + "." + nextName);
            } catch (ClassNotFoundException var10) {
            }

            if (next != null && PermissionsResolver.class.isAssignableFrom(next)) {
               this.enabledResolvers.add(next.asSubclass(PermissionsResolver.class));
            } else {
               this.logger.warning("WEPIF: Invalid or unknown class found in enabled resolvers: " + nextName + ". Moving to disabled resolvers list.");
               i.remove();
               disabledResolvers.add(nextName);
               isUpdated = true;
            }
         }

         for (Class<?> clazz : this.availableResolvers) {
            if (!stagedEnabled.contains(clazz.getSimpleName()) && !disabledResolvers.contains(clazz.getSimpleName())) {
               disabledResolvers.add(clazz.getSimpleName());
               this.logger.info("New permissions resolver: " + clazz.getSimpleName() + " detected. Added to disabled resolvers list.");
               isUpdated = true;
            }
         }

         this.config.setProperty("resolvers.disabled", disabledResolvers);
         this.config.setProperty("resolvers.enabled", stagedEnabled);
      }

      if (keys.contains("dinner-perms") || keys.contains("dinnerperms")) {
         this.config.removeProperty("dinner-perms");
         this.config.removeProperty("dinnerperms");
         isUpdated = true;
      }

      if (!keys.contains("permissions")) {
         ConfigurationPermissionsResolver.generateDefaultPerms(this.config.addNode("permissions"));
         isUpdated = true;
      }

      if (isUpdated) {
         this.logger.info("WEPIF: Updated config file");
         this.config.save();
      }

      return isUpdated;
   }

   public static class MissingPluginException extends Exception {
   }

   class ServerListener implements Listener {
      @EventHandler
      public void onPluginEnable(PluginEnableEvent event) {
         Plugin plugin = event.getPlugin();
         String name = plugin.getDescription().getName();
         if (plugin instanceof PermissionsProvider) {
            PermissionsResolverManager.this.setPluginPermissionsResolver(plugin);
         } else if ("permissions".equalsIgnoreCase(name)
            || "permissionsex".equalsIgnoreCase(name)
            || "bpermissions".equalsIgnoreCase(name)
            || "groupmanager".equalsIgnoreCase(name)) {
            PermissionsResolverManager.this.load();
         }
      }

      @EventHandler
      public void onPluginDisable(PluginDisableEvent event) {
         String name = event.getPlugin().getDescription().getName();
         if (event.getPlugin() instanceof PermissionsProvider
            || "permissions".equalsIgnoreCase(name)
            || "permissionsex".equalsIgnoreCase(name)
            || "bpermissions".equalsIgnoreCase(name)
            || "groupmanager".equalsIgnoreCase(name)) {
            PermissionsResolverManager.this.load();
         }
      }

      void register(Plugin plugin) {
         plugin.getServer().getPluginManager().registerEvents(this, plugin);
      }
   }
}
