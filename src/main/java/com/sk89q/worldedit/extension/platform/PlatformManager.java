package com.sk89q.worldedit.extension.platform;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.ServerInterface;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.command.tool.BlockTool;
import com.sk89q.worldedit.command.tool.DoubleActionBlockTool;
import com.sk89q.worldedit.command.tool.DoubleActionTraceTool;
import com.sk89q.worldedit.command.tool.Tool;
import com.sk89q.worldedit.command.tool.TraceTool;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.event.platform.BlockInteractEvent;
import com.sk89q.worldedit.event.platform.ConfigurationLoadEvent;
import com.sk89q.worldedit.event.platform.Interaction;
import com.sk89q.worldedit.event.platform.PlatformInitializeEvent;
import com.sk89q.worldedit.event.platform.PlatformReadyEvent;
import com.sk89q.worldedit.event.platform.PlayerInputEvent;
import com.sk89q.worldedit.extension.platform.permission.ActorSelectorLimits;
import com.sk89q.worldedit.internal.ServerInterfaceAdapter;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import com.sk89q.worldedit.world.World;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

public class PlatformManager {
   private static final Logger logger = Logger.getLogger(PlatformManager.class.getCanonicalName());
   private final WorldEdit worldEdit;
   private final CommandManager commandManager;
   private final List<Platform> platforms = new ArrayList<>();
   private final Map<Capability, Platform> preferences = new EnumMap<>(Capability.class);
   @Nullable
   private String firstSeenVersion;
   private final AtomicBoolean initialized = new AtomicBoolean();
   private final AtomicBoolean configured = new AtomicBoolean();

   public PlatformManager(WorldEdit worldEdit) {
      Preconditions.checkNotNull(worldEdit);
      this.worldEdit = worldEdit;
      this.commandManager = new CommandManager(worldEdit, this);
      worldEdit.getEventBus().register(this);
   }

   public synchronized void register(Platform platform) {
      Preconditions.checkNotNull(platform);
      logger.log(Level.FINE, "Got request to register " + platform.getClass() + " with WorldEdit [" + super.toString() + "]");
      this.platforms.add(platform);
      if (this.firstSeenVersion != null) {
         if (!this.firstSeenVersion.equals(platform.getVersion())) {
            logger.log(
               Level.WARNING,
               "Multiple ports of WorldEdit are installed but they report different versions ({0} and {1}). If these two versions are truly different, then you may run into unexpected crashes and errors.",
               new Object[]{this.firstSeenVersion, platform.getVersion()}
            );
         }
      } else {
         this.firstSeenVersion = platform.getVersion();
      }
   }

   public synchronized boolean unregister(Platform platform) {
      Preconditions.checkNotNull(platform);
      boolean removed = this.platforms.remove(platform);
      if (removed) {
         logger.log(Level.FINE, "Unregistering " + platform.getClass().getCanonicalName() + " from WorldEdit");
         boolean choosePreferred = false;
         Iterator<Entry<Capability, Platform>> it = this.preferences.entrySet().iterator();

         while (it.hasNext()) {
            Entry<Capability, Platform> entry = it.next();
            if (entry.getValue().equals(platform)) {
               entry.getKey().unload(this, entry.getValue());
               it.remove();
               choosePreferred = true;
            }
         }

         if (choosePreferred) {
            this.choosePreferred();
         }
      }

      return removed;
   }

   public synchronized Platform queryCapability(Capability capability) throws NoCapablePlatformException {
      Platform platform = this.preferences.get(Preconditions.checkNotNull(capability));
      if (platform != null) {
         return platform;
      } else {
         throw new NoCapablePlatformException("No platform was found supporting " + capability.name());
      }
   }

   private synchronized void choosePreferred() {
      for (Capability capability : Capability.values()) {
         Platform preferred = this.findMostPreferred(capability);
         if (preferred != null) {
            this.preferences.put(capability, preferred);
            capability.initialize(this, preferred);
         }
      }

      if (this.preferences.containsKey(Capability.CONFIGURATION) && this.configured.compareAndSet(false, true)) {
         this.worldEdit.getEventBus().post(new ConfigurationLoadEvent(this.queryCapability(Capability.CONFIGURATION).getConfiguration()));
      }
   }

   @Nullable
   private synchronized Platform findMostPreferred(Capability capability) {
      Platform preferred = null;
      Preference highest = null;

      for (Platform platform : this.platforms) {
         Preference preference = platform.getCapabilities().get(capability);
         if (preference != null && (highest == null || preference.isPreferredOver(highest))) {
            preferred = platform;
            highest = preference;
         }
      }

      return preferred;
   }

   public synchronized List<Platform> getPlatforms() {
      return new ArrayList<>(this.platforms);
   }

   public World getWorldForEditing(World base) {
      Preconditions.checkNotNull(base);
      World match = this.queryCapability(Capability.WORLD_EDITING).matchWorld(base);
      return match != null ? match : base;
   }

   public <T extends Actor> T createProxyActor(T base) {
      Preconditions.checkNotNull(base);
      if (base instanceof Player) {
         Player player = (Player)base;
         Player permActor = this.queryCapability(Capability.PERMISSIONS).matchPlayer(player);
         if (permActor == null) {
            permActor = player;
         }

         Player cuiActor = this.queryCapability(Capability.WORLDEDIT_CUI).matchPlayer(player);
         if (cuiActor == null) {
            cuiActor = player;
         }

         return (T)(new PlayerProxy(player, permActor, cuiActor, this.getWorldForEditing(player.getWorld())));
      } else {
         return base;
      }
   }

   public CommandManager getCommandManager() {
      return this.commandManager;
   }

   public LocalConfiguration getConfiguration() {
      return this.queryCapability(Capability.CONFIGURATION).getConfiguration();
   }

   public ServerInterface getServerInterface() throws IllegalStateException {
      return ServerInterfaceAdapter.adapt(this.queryCapability(Capability.USER_COMMANDS));
   }

   @Subscribe
   public void handlePlatformReady(PlatformReadyEvent event) {
      this.choosePreferred();
      if (this.initialized.compareAndSet(false, true)) {
         this.worldEdit.getEventBus().post(new PlatformInitializeEvent());
      }
   }

   @Subscribe
   public void handleBlockInteract(BlockInteractEvent event) {
      Actor actor = this.createProxyActor(event.getCause());
      Location location = event.getLocation();
      Vector vector = location.toVector();
      if (actor instanceof Player) {
         Player player = (Player)actor;
         LocalSession session = this.worldEdit.getSessionManager().get(actor);
         if (event.getType() == Interaction.HIT) {
            if (player.getItemInHand() == this.getConfiguration().wandItem) {
               if (!session.isToolControlEnabled()) {
                  return;
               }

               if (!actor.hasPermission("worldedit.selection.pos")) {
                  return;
               }

               RegionSelector selector = session.getRegionSelector(player.getWorld());
               if (selector.selectPrimary(location.toVector(), ActorSelectorLimits.forActor(player))) {
                  selector.explainPrimarySelection(actor, session, vector);
               }

               event.setCancelled(true);
               return;
            }

            if (player.isHoldingPickAxe() && session.hasSuperPickAxe()) {
               BlockTool superPickaxe = session.getSuperPickaxe();
               if (superPickaxe != null && superPickaxe.canUse(player)) {
                  event.setCancelled(
                     superPickaxe.actPrimary(this.queryCapability(Capability.WORLD_EDITING), this.getConfiguration(), player, session, location)
                  );
                  return;
               }
            }

            Tool tool = session.getTool(player.getItemInHand());
            if (tool != null && tool instanceof DoubleActionBlockTool && tool.canUse(player)) {
               ((DoubleActionBlockTool)tool).actSecondary(this.queryCapability(Capability.WORLD_EDITING), this.getConfiguration(), player, session, location);
               event.setCancelled(true);
            }
         } else if (event.getType() == Interaction.OPEN) {
            if (player.getItemInHand() == this.getConfiguration().wandItem) {
               if (!session.isToolControlEnabled()) {
                  return;
               }

               if (!actor.hasPermission("worldedit.selection.pos")) {
                  return;
               }

               RegionSelector selector = session.getRegionSelector(player.getWorld());
               if (selector.selectSecondary(vector, ActorSelectorLimits.forActor(player))) {
                  selector.explainSecondarySelection(actor, session, vector);
               }

               event.setCancelled(true);
               return;
            }

            Tool tool = session.getTool(player.getItemInHand());
            if (tool != null && tool instanceof BlockTool && tool.canUse(player)) {
               ((BlockTool)tool).actPrimary(this.queryCapability(Capability.WORLD_EDITING), this.getConfiguration(), player, session, location);
               event.setCancelled(true);
            }
         }
      }
   }

   @Subscribe
   public void handlePlayerInput(PlayerInputEvent event) {
      Player player = this.createProxyActor(event.getPlayer());
      switch (event.getInputType()) {
         case PRIMARY: {
            if (player.getItemInHand() == this.getConfiguration().navigationWand) {
               if (this.getConfiguration().navigationWandMaxDistance <= 0) {
                  return;
               }

               if (!player.hasPermission("worldedit.navigation.jumpto.tool")) {
                  return;
               }

               WorldVector pos = player.getSolidBlockTrace(this.getConfiguration().navigationWandMaxDistance);
               if (pos != null) {
                  player.findFreePosition(pos);
               } else {
                  player.printError("No block in sight (or too far)!");
               }

               event.setCancelled(true);
               return;
            }

            LocalSession session = this.worldEdit.getSessionManager().get(player);
            Tool tool = session.getTool(player.getItemInHand());
            if (tool != null && tool instanceof DoubleActionTraceTool && tool.canUse(player)) {
               ((DoubleActionTraceTool)tool).actSecondary(this.queryCapability(Capability.WORLD_EDITING), this.getConfiguration(), player, session);
               event.setCancelled(true);
               return;
            }
            break;
         }
         case SECONDARY: {
            if (player.getItemInHand() == this.getConfiguration().navigationWand) {
               if (this.getConfiguration().navigationWandMaxDistance <= 0) {
                  return;
               }

               if (!player.hasPermission("worldedit.navigation.thru.tool")) {
                  return;
               }

               if (!player.passThroughForwardWall(40)) {
                  player.printError("Nothing to pass through!");
               }

               event.setCancelled(true);
               return;
            }

            LocalSession session = this.worldEdit.getSessionManager().get(player);
            Tool tool = session.getTool(player.getItemInHand());
            if (tool != null && tool instanceof TraceTool && tool.canUse(player)) {
               ((TraceTool)tool).actPrimary(this.queryCapability(Capability.WORLD_EDITING), this.getConfiguration(), player, session);
               event.setCancelled(true);
               return;
            }
         }
      }
   }
}
