package com.sk89q.worldedit.bukkit;

import com.sk89q.util.StringUtil;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.ServerInterface;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.internal.cui.CUIEvent;
import com.sk89q.worldedit.session.SessionKey;
import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BukkitPlayer extends LocalPlayer {
   private Player player;
   private WorldEditPlugin plugin;

   public BukkitPlayer(WorldEditPlugin plugin, ServerInterface server, Player player) {
      this.plugin = plugin;
      this.player = player;
   }

   @Override
   public UUID getUniqueId() {
      return this.player.getUniqueId();
   }

   @Override
   public int getItemInHand() {
      ItemStack itemStack = this.player.getItemInHand();
      return itemStack != null ? itemStack.getTypeId() : 0;
   }

   @Override
   public BaseBlock getBlockInHand() throws WorldEditException {
      ItemStack itemStack = this.player.getItemInHand();
      return BukkitUtil.toBlock(this.getWorld(), itemStack);
   }

   @Override
   public String getName() {
      return this.player.getName();
   }

   @Override
   public WorldVector getPosition() {
      Location loc = this.player.getLocation();
      return new WorldVector(BukkitUtil.getLocalWorld(loc.getWorld()), loc.getX(), loc.getY(), loc.getZ());
   }

   @Override
   public double getPitch() {
      return this.player.getLocation().getPitch();
   }

   @Override
   public double getYaw() {
      return this.player.getLocation().getYaw();
   }

   @Override
   public void giveItem(int type, int amt) {
      this.player.getInventory().addItem(new ItemStack[]{new ItemStack(type, amt)});
   }

   @Override
   public void printRaw(String msg) {
      for (String part : msg.split("\n")) {
         this.player.sendMessage(part);
      }
   }

   @Override
   public void print(String msg) {
      for (String part : msg.split("\n")) {
         this.player.sendMessage("§d" + part);
      }
   }

   @Override
   public void printDebug(String msg) {
      for (String part : msg.split("\n")) {
         this.player.sendMessage("§7" + part);
      }
   }

   @Override
   public void printError(String msg) {
      for (String part : msg.split("\n")) {
         this.player.sendMessage("§c" + part);
      }
   }

   @Override
   public void setPosition(Vector pos, float pitch, float yaw) {
      this.player.teleport(new Location(this.player.getWorld(), pos.getX(), pos.getY(), pos.getZ(), yaw, pitch));
   }

   @Override
   public String[] getGroups() {
      return this.plugin.getPermissionsResolver().getGroups(this.player);
   }

   @Override
   public BlockBag getInventoryBlockBag() {
      return new BukkitPlayerBlockBag(this.player);
   }

   @Override
   public boolean hasPermission(String perm) {
      return !this.plugin.getLocalConfiguration().noOpPermissions && this.player.isOp()
         || this.plugin.getPermissionsResolver().hasPermission(this.player.getWorld().getName(), this.player, perm);
   }

   public LocalWorld getWorld() {
      return BukkitUtil.getLocalWorld(this.player.getWorld());
   }

   @Override
   public void dispatchCUIEvent(CUIEvent event) {
      String[] params = event.getParameters();
      String send = event.getTypeId();
      if (params.length > 0) {
         send = send + "|" + StringUtil.joinString(params, "|");
      }

      this.player.sendPluginMessage(this.plugin, "WECUI", send.getBytes(CUIChannelListener.UTF_8_CHARSET));
   }

   public Player getPlayer() {
      return this.player;
   }

   @Override
   public boolean hasCreativeMode() {
      return this.player.getGameMode() == GameMode.CREATIVE;
   }

   @Override
   public void floatAt(int x, int y, int z, boolean alwaysGlass) {
      if (!alwaysGlass && this.player.getAllowFlight()) {
         this.setPosition(new Vector(x + 0.5, (double)y, z + 0.5));
         this.player.setFlying(true);
      } else {
         super.floatAt(x, y, z, alwaysGlass);
      }
   }

   @Override
   public BaseEntity getState() {
      throw new UnsupportedOperationException("Cannot create a state from this object");
   }

   @Override
   public com.sk89q.worldedit.util.Location getLocation() {
      Location nativeLocation = this.player.getLocation();
      Vector position = BukkitUtil.toVector(nativeLocation);
      return new com.sk89q.worldedit.util.Location(this.getWorld(), position, nativeLocation.getYaw(), nativeLocation.getPitch());
   }

   @Nullable
   @Override
   public <T> T getFacet(Class<? extends T> cls) {
      return null;
   }

   @Override
   public SessionKey getSessionKey() {
      return new BukkitPlayer.SessionKeyImpl(this.player.getUniqueId(), this.player.getName());
   }

   private static class SessionKeyImpl implements SessionKey {
      private final UUID uuid;
      private final String name;

      private SessionKeyImpl(UUID uuid, String name) {
         this.uuid = uuid;
         this.name = name;
      }

      @Override
      public UUID getUniqueId() {
         return this.uuid;
      }

      @Nullable
      @Override
      public String getName() {
         return this.name;
      }

      @Override
      public boolean isActive() {
         return Bukkit.getServer().getPlayerExact(this.name) != null;
      }

      @Override
      public boolean isPersistent() {
         return true;
      }
   }
}
