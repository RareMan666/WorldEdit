package com.sk89q.worldedit.extension.platform;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.internal.cui.CUIEvent;
import com.sk89q.worldedit.session.SessionKey;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import java.util.UUID;
import javax.annotation.Nullable;

class PlayerProxy extends AbstractPlayerActor {
   private final Player basePlayer;
   private final Actor permActor;
   private final Actor cuiActor;
   private final World world;

   PlayerProxy(Player basePlayer, Actor permActor, Actor cuiActor, World world) {
      Preconditions.checkNotNull(basePlayer);
      Preconditions.checkNotNull(permActor);
      Preconditions.checkNotNull(cuiActor);
      Preconditions.checkNotNull(world);
      this.basePlayer = basePlayer;
      this.permActor = permActor;
      this.cuiActor = cuiActor;
      this.world = world;
   }

   @Override
   public UUID getUniqueId() {
      return this.basePlayer.getUniqueId();
   }

   @Override
   public int getItemInHand() {
      return this.basePlayer.getItemInHand();
   }

   @Override
   public void giveItem(int type, int amount) {
      this.basePlayer.giveItem(type, amount);
   }

   @Override
   public BlockBag getInventoryBlockBag() {
      return this.basePlayer.getInventoryBlockBag();
   }

   @Override
   public String getName() {
      return this.basePlayer.getName();
   }

   @Override
   public BaseEntity getState() {
      throw new UnsupportedOperationException("Can't getState() on a player");
   }

   @Override
   public Location getLocation() {
      return this.basePlayer.getLocation();
   }

   @Override
   public WorldVector getPosition() {
      return this.basePlayer.getPosition();
   }

   @Override
   public double getPitch() {
      return this.basePlayer.getPitch();
   }

   @Override
   public double getYaw() {
      return this.basePlayer.getYaw();
   }

   @Override
   public void setPosition(Vector pos, float pitch, float yaw) {
      this.basePlayer.setPosition(pos, pitch, yaw);
   }

   @Override
   public World getWorld() {
      return this.world;
   }

   @Override
   public void printRaw(String msg) {
      this.basePlayer.printRaw(msg);
   }

   @Override
   public void printDebug(String msg) {
      this.basePlayer.printDebug(msg);
   }

   @Override
   public void print(String msg) {
      this.basePlayer.print(msg);
   }

   @Override
   public void printError(String msg) {
      this.basePlayer.printError(msg);
   }

   @Override
   public String[] getGroups() {
      return this.permActor.getGroups();
   }

   @Override
   public boolean hasPermission(String perm) {
      return this.permActor.hasPermission(perm);
   }

   @Override
   public void dispatchCUIEvent(CUIEvent event) {
      this.cuiActor.dispatchCUIEvent(event);
   }

   @Nullable
   @Override
   public <T> T getFacet(Class<? extends T> cls) {
      return this.basePlayer.getFacet(cls);
   }

   @Override
   public SessionKey getSessionKey() {
      return this.basePlayer.getSessionKey();
   }
}
