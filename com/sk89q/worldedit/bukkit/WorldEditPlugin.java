package com.sk89q.worldedit.bukkit;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.sk89q.util.yaml.YAMLProcessor;
import com.sk89q.wepif.PermissionsResolverManager;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.ServerInterface;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditOperation;
import com.sk89q.worldedit.bukkit.adapter.AdapterLoadException;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplLoader;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.CylinderSelection;
import com.sk89q.worldedit.bukkit.selections.Polygonal2DSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.event.platform.CommandEvent;
import com.sk89q.worldedit.event.platform.CommandSuggestionEvent;
import com.sk89q.worldedit.event.platform.PlatformReadyEvent;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Capability;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.util.Java8Detector;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import javax.annotation.Nullable;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldEditPlugin extends JavaPlugin implements TabCompleter {
   private static final Logger log = Logger.getLogger(WorldEditPlugin.class.getCanonicalName());
   public static final String CUI_PLUGIN_CHANNEL = "WECUI";
   private static WorldEditPlugin INSTANCE;
   private BukkitImplAdapter bukkitAdapter;
   private BukkitServerInterface server;
   private final WorldEditAPI api = new WorldEditAPI(this);
   private BukkitConfiguration config;

   public void onEnable() {
      INSTANCE = this;
      this.getDataFolder().mkdirs();
      WorldEdit worldEdit = WorldEdit.getInstance();
      this.loadConfig();
      PermissionsResolverManager.initialize(this);
      this.server = new BukkitServerInterface(this, this.getServer());
      worldEdit.getPlatformManager().register(this.server);
      this.getServer().getMessenger().registerIncomingPluginChannel(this, "WECUI", new CUIChannelListener(this));
      this.getServer().getMessenger().registerOutgoingPluginChannel(this, "WECUI");
      this.getServer().getPluginManager().registerEvents(new WorldEditListener(this), this);
      WorldEdit.getInstance().getEventBus().post(new PlatformReadyEvent());
      this.loadAdapter();
      Java8Detector.notifyIfNot8();
   }

   private void loadConfig() {
      this.createDefaultConfiguration("config.yml");
      this.config = new BukkitConfiguration(new YAMLProcessor(new File(this.getDataFolder(), "config.yml"), true), this);
      this.config.load();
   }

   private void loadAdapter() {
      WorldEdit worldEdit = WorldEdit.getInstance();
      BukkitImplLoader adapterLoader = new BukkitImplLoader();

      try {
         adapterLoader.addFromPath(this.getClass().getClassLoader());
      } catch (IOException var6) {
         log.log(Level.WARNING, "Failed to search path for Bukkit adapters");
      }

      try {
         adapterLoader.addFromJar(this.getFile());
      } catch (IOException var5) {
         log.log(Level.WARNING, "Failed to search " + this.getFile() + " for Bukkit adapters", (Throwable)var5);
      }

      try {
         this.bukkitAdapter = adapterLoader.loadAdapter();
         log.log(Level.INFO, "Using " + this.bukkitAdapter.getClass().getCanonicalName() + " as the Bukkit adapter");
      } catch (AdapterLoadException var7) {
         Platform platform = worldEdit.getPlatformManager().queryCapability(Capability.WORLD_EDITING);
         if (platform instanceof BukkitServerInterface) {
            log.log(Level.WARNING, var7.getMessage());
         } else {
            log.log(
               Level.INFO,
               "WorldEdit could not find a Bukkit adapter for this MC version, but it seems that you have another implementation of WorldEdit installed ("
                  + platform.getPlatformName()
                  + ") that handles the world editing."
            );
         }
      }
   }

   public void onDisable() {
      WorldEdit worldEdit = WorldEdit.getInstance();
      worldEdit.clearSessions();
      worldEdit.getPlatformManager().unregister(this.server);
      if (this.config != null) {
         this.config.unload();
      }

      if (this.server != null) {
         this.server.unregisterCommands();
      }

      this.getServer().getScheduler().cancelTasks(this);
   }

   protected void loadConfiguration() {
      this.config.unload();
      this.config.load();
      this.getPermissionsResolver().load();
   }

   protected void createDefaultConfiguration(String name) {
      File actual = new File(this.getDataFolder(), name);
      if (!actual.exists()) {
         InputStream input = null;

         try {
            JarFile file = new JarFile(this.getFile());
            ZipEntry copy = file.getEntry("defaults/" + name);
            if (copy == null) {
               throw new FileNotFoundException();
            }

            input = file.getInputStream(copy);
         } catch (IOException var20) {
            this.getLogger().severe("Unable to read default configuration: " + name);
         }

         if (input != null) {
            FileOutputStream output = null;

            try {
               output = new FileOutputStream(actual);
               byte[] buf = new byte[8192];

               int length;
               while ((length = input.read(buf)) > 0) {
                  output.write(buf, 0, length);
               }

               this.getLogger().info("Default configuration file written: " + name);
            } catch (IOException var21) {
               this.getLogger().log(Level.WARNING, "Failed to write default config file", (Throwable)var21);
            } finally {
               try {
                  input.close();
               } catch (IOException var19) {
               }

               try {
                  if (output != null) {
                     output.close();
                  }
               } catch (IOException var18) {
               }
            }
         }
      }
   }

   public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
      String[] split = new String[args.length + 1];
      System.arraycopy(args, 0, split, 1, args.length);
      split[0] = cmd.getName();
      CommandEvent event = new CommandEvent(this.wrapCommandSender(sender), Joiner.on(" ").join(split));
      this.getWorldEdit().getEventBus().post(event);
      return true;
   }

   public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
      String[] split = new String[args.length + 1];
      System.arraycopy(args, 0, split, 1, args.length);
      split[0] = cmd.getName();
      CommandSuggestionEvent event = new CommandSuggestionEvent(this.wrapCommandSender(sender), Joiner.on(" ").join(split));
      this.getWorldEdit().getEventBus().post(event);
      return event.getSuggestions();
   }

   public LocalSession getSession(Player player) {
      return WorldEdit.getInstance().getSession(this.wrapPlayer(player));
   }

   public EditSession createEditSession(Player player) {
      LocalPlayer wePlayer = this.wrapPlayer(player);
      LocalSession session = WorldEdit.getInstance().getSession(wePlayer);
      BlockBag blockBag = session.getBlockBag(wePlayer);
      EditSession editSession = WorldEdit.getInstance()
         .getEditSessionFactory()
         .getEditSession(wePlayer.getWorld(), session.getBlockChangeLimit(), blockBag, wePlayer);
      editSession.enableQueue();
      return editSession;
   }

   public void remember(Player player, EditSession editSession) {
      LocalPlayer wePlayer = this.wrapPlayer(player);
      LocalSession session = WorldEdit.getInstance().getSession(wePlayer);
      session.remember(editSession);
      editSession.flushQueue();
      WorldEdit.getInstance().flushBlockBag(wePlayer, editSession);
   }

   @Deprecated
   public void perform(Player player, WorldEditOperation op) throws Throwable {
      LocalPlayer wePlayer = this.wrapPlayer(player);
      LocalSession session = WorldEdit.getInstance().getSession(wePlayer);
      EditSession editSession = this.createEditSession(player);

      try {
         op.run(session, wePlayer, editSession);
      } finally {
         this.remember(player, editSession);
      }
   }

   @Deprecated
   public WorldEditAPI getAPI() {
      return this.api;
   }

   public BukkitConfiguration getLocalConfiguration() {
      return this.config;
   }

   public PermissionsResolverManager getPermissionsResolver() {
      return PermissionsResolverManager.getInstance();
   }

   public BukkitPlayer wrapPlayer(Player player) {
      return new BukkitPlayer(this, this.server, player);
   }

   public Actor wrapCommandSender(CommandSender sender) {
      return (Actor)(sender instanceof Player ? this.wrapPlayer((Player)sender) : new BukkitCommandSender(this, sender));
   }

   public ServerInterface getServerInterface() {
      return this.server;
   }

   BukkitServerInterface getInternalPlatform() {
      return this.server;
   }

   public WorldEdit getWorldEdit() {
      return WorldEdit.getInstance();
   }

   public Selection getSelection(Player player) {
      if (player == null) {
         throw new IllegalArgumentException("Null player not allowed");
      } else if (!player.isOnline()) {
         throw new IllegalArgumentException("Offline player not allowed");
      } else {
         LocalSession session = WorldEdit.getInstance().getSession(this.wrapPlayer(player));
         RegionSelector selector = session.getRegionSelector(BukkitUtil.getLocalWorld(player.getWorld()));

         try {
            Region region = selector.getRegion();
            World world = BukkitAdapter.asBukkitWorld(session.getSelectionWorld()).getWorld();
            if (region instanceof CuboidRegion) {
               return new CuboidSelection(world, selector, (CuboidRegion)region);
            } else if (region instanceof Polygonal2DRegion) {
               return new Polygonal2DSelection(world, selector, (Polygonal2DRegion)region);
            } else {
               return region instanceof CylinderRegion ? new CylinderSelection(world, selector, (CylinderRegion)region) : null;
            }
         } catch (IncompleteRegionException var6) {
            return null;
         }
      }
   }

   public void setSelection(Player player, Selection selection) {
      if (player == null) {
         throw new IllegalArgumentException("Null player not allowed");
      } else if (!player.isOnline()) {
         throw new IllegalArgumentException("Offline player not allowed");
      } else if (selection == null) {
         throw new IllegalArgumentException("Null selection not allowed");
      } else {
         LocalSession session = WorldEdit.getInstance().getSession(this.wrapPlayer(player));
         RegionSelector sel = selection.getRegionSelector();
         session.setRegionSelector(BukkitUtil.getLocalWorld(player.getWorld()), sel);
         session.dispatchCUISelection(this.wrapPlayer(player));
      }
   }

   static WorldEditPlugin getInstance() {
      return (WorldEditPlugin)Preconditions.checkNotNull(INSTANCE);
   }

   @Nullable
   BukkitImplAdapter getBukkitImplAdapter() {
      return this.bukkitAdapter;
   }
}
