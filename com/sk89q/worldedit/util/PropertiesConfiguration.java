package com.sk89q.worldedit.util;

import com.sk89q.util.StringUtil;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.world.snapshot.SnapshotRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesConfiguration extends LocalConfiguration {
   private static final Logger log = Logger.getLogger(PropertiesConfiguration.class.getCanonicalName());
   protected Properties properties;
   protected File path;

   public PropertiesConfiguration(File path) {
      this.path = path;
      this.properties = new Properties();
   }

   @Override
   public void load() {
      InputStream stream = null;

      try {
         stream = new FileInputStream(this.path);
         this.properties.load(stream);
      } catch (FileNotFoundException var36) {
      } catch (IOException var37) {
         log.log(Level.WARNING, "Failed to read configuration", (Throwable)var37);
      } finally {
         if (stream != null) {
            try {
               stream.close();
            } catch (IOException var33) {
            }
         }
      }

      this.loadExtra();
      this.profile = this.getBool("profile", this.profile);
      this.disallowedBlocks = this.getIntSet("disallowed-blocks", defaultDisallowedBlocks);
      this.defaultChangeLimit = this.getInt("default-max-changed-blocks", this.defaultChangeLimit);
      this.maxChangeLimit = this.getInt("max-changed-blocks", this.maxChangeLimit);
      this.defaultMaxPolygonalPoints = this.getInt("default-max-polygon-points", this.defaultMaxPolygonalPoints);
      this.maxPolygonalPoints = this.getInt("max-polygon-points", this.maxPolygonalPoints);
      this.defaultMaxPolyhedronPoints = this.getInt("default-max-polyhedron-points", this.defaultMaxPolyhedronPoints);
      this.maxPolyhedronPoints = this.getInt("max-polyhedron-points", this.maxPolyhedronPoints);
      this.shellSaveType = this.getString("shell-save-type", this.shellSaveType);
      this.maxRadius = this.getInt("max-radius", this.maxRadius);
      this.maxSuperPickaxeSize = this.getInt("max-super-pickaxe-size", this.maxSuperPickaxeSize);
      this.maxBrushRadius = this.getInt("max-brush-radius", this.maxBrushRadius);
      this.logCommands = this.getBool("log-commands", this.logCommands);
      this.logFile = this.getString("log-file", this.logFile);
      this.registerHelp = this.getBool("register-help", this.registerHelp);
      this.wandItem = this.getInt("wand-item", this.wandItem);
      this.superPickaxeDrop = this.getBool("super-pickaxe-drop-items", this.superPickaxeDrop);
      this.superPickaxeManyDrop = this.getBool("super-pickaxe-many-drop-items", this.superPickaxeManyDrop);
      this.noDoubleSlash = this.getBool("no-double-slash", this.noDoubleSlash);
      this.useInventory = this.getBool("use-inventory", this.useInventory);
      this.useInventoryOverride = this.getBool("use-inventory-override", this.useInventoryOverride);
      this.useInventoryCreativeOverride = this.getBool("use-inventory-creative-override", this.useInventoryCreativeOverride);
      this.navigationWand = this.getInt("nav-wand-item", this.navigationWand);
      this.navigationWandMaxDistance = this.getInt("nav-wand-distance", this.navigationWandMaxDistance);
      this.navigationUseGlass = this.getBool("nav-use-glass", this.navigationUseGlass);
      this.scriptTimeout = this.getInt("scripting-timeout", this.scriptTimeout);
      this.saveDir = this.getString("schematic-save-dir", this.saveDir);
      this.scriptsDir = this.getString("craftscript-dir", this.scriptsDir);
      this.butcherDefaultRadius = this.getInt("butcher-default-radius", this.butcherDefaultRadius);
      this.butcherMaxRadius = this.getInt("butcher-max-radius", this.butcherMaxRadius);
      this.allowSymlinks = this.getBool("allow-symbolic-links", this.allowSymlinks);
      LocalSession.MAX_HISTORY_SIZE = Math.max(15, this.getInt("history-size", 15));
      String snapshotsDir = this.getString("snapshots-dir", "");
      if (!snapshotsDir.isEmpty()) {
         this.snapshotRepo = new SnapshotRepository(snapshotsDir);
      }

      OutputStream output = null;
      this.path.getParentFile().mkdirs();

      try {
         output = new FileOutputStream(this.path);
         this.properties.store(output, "Don't put comments; they get removed");
      } catch (FileNotFoundException var34) {
         log.log(Level.WARNING, "Failed to write configuration", (Throwable)var34);
      } catch (IOException var35) {
         log.log(Level.WARNING, "Failed to write configuration", (Throwable)var35);
      } finally {
         if (output != null) {
            try {
               output.close();
            } catch (IOException var32) {
            }
         }
      }
   }

   protected void loadExtra() {
   }

   protected String getString(String key, String def) {
      if (def == null) {
         def = "";
      }

      String val = this.properties.getProperty(key);
      if (val == null) {
         this.properties.setProperty(key, def);
         return def;
      } else {
         return val;
      }
   }

   protected boolean getBool(String key, boolean def) {
      String val = this.properties.getProperty(key);
      if (val == null) {
         this.properties.setProperty(key, def ? "true" : "false");
         return def;
      } else {
         return val.equalsIgnoreCase("true") || val.equals("1");
      }
   }

   protected int getInt(String key, int def) {
      String val = this.properties.getProperty(key);
      if (val == null) {
         this.properties.setProperty(key, String.valueOf(def));
         return def;
      } else {
         try {
            return Integer.parseInt(val);
         } catch (NumberFormatException var5) {
            this.properties.setProperty(key, String.valueOf(def));
            return def;
         }
      }
   }

   protected double getDouble(String key, double def) {
      String val = this.properties.getProperty(key);
      if (val == null) {
         this.properties.setProperty(key, String.valueOf(def));
         return def;
      } else {
         try {
            return Double.parseDouble(val);
         } catch (NumberFormatException var6) {
            this.properties.setProperty(key, String.valueOf(def));
            return def;
         }
      }
   }

   protected Set<Integer> getIntSet(String key, int[] def) {
      String val = this.properties.getProperty(key);
      if (val == null) {
         this.properties.setProperty(key, StringUtil.joinString(def, ",", 0));
         Set<Integer> set = new HashSet<>();

         for (int i : def) {
            set.add(i);
         }

         return set;
      } else {
         Set<Integer> set = new HashSet<>();
         String[] parts = val.split(",");

         for (String part : parts) {
            try {
               int v = Integer.parseInt(part.trim());
               set.add(v);
            } catch (NumberFormatException var11) {
            }
         }

         return set;
      }
   }
}
