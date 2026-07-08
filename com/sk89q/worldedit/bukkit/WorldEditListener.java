package com.sk89q.worldedit.bukkit;

import com.sk89q.util.StringUtil;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.internal.LocalWorldAdapter;
import com.sk89q.worldedit.world.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class WorldEditListener implements Listener {
   private WorldEditPlugin plugin;

   // PlayerInteractEvent.getHand() and EquipmentSlot were added in Bukkit 1.9.
   // On 1.8.8 the method is absent, so calling it throws NoSuchMethodError on
   // every interact. The previous try/catch swallowed the error but the JVM
   // still built the stack trace each time (~8ms per slow tick in profiling).
   // Resolve once at class load and skip the call entirely on legacy servers.
   private static final boolean SUPPORTS_OFF_HAND;
   static {
      boolean supports;
      try {
         PlayerInteractEvent.class.getMethod("getHand");
         supports = true;
      } catch (NoSuchMethodException e) {
         supports = false;
      }
      SUPPORTS_OFF_HAND = supports;
   }

   public WorldEditListener(WorldEditPlugin plugin) {
      this.plugin = plugin;
   }

   @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
   public void onGamemode(PlayerGameModeChangeEvent event) {
      if (this.plugin.getInternalPlatform().isHookingEvents()) {
         WorldEdit.getInstance().getSession(this.plugin.wrapPlayer(event.getPlayer()));
      }
   }

   @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
   public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
      String[] split = event.getMessage().split(" ");
      if (split.length > 0) {
         split[0] = split[0].substring(1);
         split = this.plugin.getWorldEdit().getPlatformManager().getCommandManager().commandDetection(split);
      }

      String newMessage = "/" + StringUtil.joinString(split, " ");
      if (!newMessage.equals(event.getMessage())) {
         event.setMessage(newMessage);
         this.plugin.getServer().getPluginManager().callEvent(event);
         if (!event.isCancelled()) {
            if (!event.getMessage().isEmpty()) {
               this.plugin.getServer().dispatchCommand(event.getPlayer(), event.getMessage().substring(1));
            }

            event.setCancelled(true);
         }
      }
   }

   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event) {
      if (this.plugin.getInternalPlatform().isHookingEvents()) {
         if (event.useItemInHand() != Result.DENY) {
            if (SUPPORTS_OFF_HAND && event.getHand() == EquipmentSlot.OFF_HAND) {
               return;
            }

            LocalPlayer player = this.plugin.wrapPlayer(event.getPlayer());
            World world = player.getWorld();
            WorldEdit we = this.plugin.getWorldEdit();
            Action action = event.getAction();
            if (action == Action.LEFT_CLICK_BLOCK) {
               Block clickedBlock = event.getClickedBlock();
               WorldVector pos = new WorldVector(LocalWorldAdapter.adapt(world), clickedBlock.getX(), clickedBlock.getY(), clickedBlock.getZ());
               if (we.handleBlockLeftClick(player, pos)) {
                  event.setCancelled(true);
               }

               if (we.handleArmSwing(player)) {
                  event.setCancelled(true);
               }
            } else if (action == Action.LEFT_CLICK_AIR) {
               if (we.handleArmSwing(player)) {
                  event.setCancelled(true);
               }
            } else if (action == Action.RIGHT_CLICK_BLOCK) {
               Block clickedBlockx = event.getClickedBlock();
               WorldVector posx = new WorldVector(LocalWorldAdapter.adapt(world), clickedBlockx.getX(), clickedBlockx.getY(), clickedBlockx.getZ());
               if (we.handleBlockRightClick(player, posx)) {
                  event.setCancelled(true);
               }

               if (we.handleRightClick(player)) {
                  event.setCancelled(true);
               }
            } else if (action == Action.RIGHT_CLICK_AIR && we.handleRightClick(player)) {
               event.setCancelled(true);
            }
         }
      }
   }
}
