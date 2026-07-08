package com.sk89q.worldedit.bukkit;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.internal.cui.CUIEvent;
import com.sk89q.worldedit.session.SessionKey;
import com.sk89q.worldedit.util.auth.AuthorizationException;
import java.io.File;
import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BukkitCommandSender implements Actor {
   private static final UUID DEFAULT_ID = UUID.fromString("a233eb4b-4cab-42cd-9fd9-7e7b9a3f74be");
   private CommandSender sender;
   private WorldEditPlugin plugin;

   public BukkitCommandSender(WorldEditPlugin plugin, CommandSender sender) {
      Preconditions.checkNotNull(plugin);
      Preconditions.checkNotNull(sender);
      Preconditions.checkArgument(!(sender instanceof Player), "Cannot wrap a player");
      this.plugin = plugin;
      this.sender = sender;
   }

   @Override
   public UUID getUniqueId() {
      return DEFAULT_ID;
   }

   @Override
   public String getName() {
      return this.sender.getName();
   }

   @Override
   public void printRaw(String msg) {
      for (String part : msg.split("\n")) {
         this.sender.sendMessage(part);
      }
   }

   @Override
   public void print(String msg) {
      for (String part : msg.split("\n")) {
         this.sender.sendMessage("§d" + part);
      }
   }

   @Override
   public void printDebug(String msg) {
      for (String part : msg.split("\n")) {
         this.sender.sendMessage("§7" + part);
      }
   }

   @Override
   public void printError(String msg) {
      for (String part : msg.split("\n")) {
         this.sender.sendMessage("§c" + part);
      }
   }

   @Override
   public boolean canDestroyBedrock() {
      return true;
   }

   @Override
   public String[] getGroups() {
      return new String[0];
   }

   @Override
   public boolean hasPermission(String perm) {
      return true;
   }

   @Override
   public void checkPermission(String permission) throws AuthorizationException {
   }

   @Override
   public boolean isPlayer() {
      return false;
   }

   @Override
   public File openFileOpenDialog(String[] extensions) {
      return null;
   }

   @Override
   public File openFileSaveDialog(String[] extensions) {
      return null;
   }

   @Override
   public void dispatchCUIEvent(CUIEvent event) {
   }

   @Override
   public SessionKey getSessionKey() {
      return new SessionKey() {
         @Nullable
         @Override
         public String getName() {
            return null;
         }

         @Override
         public boolean isActive() {
            return false;
         }

         @Override
         public boolean isPersistent() {
            return false;
         }

         @Override
         public UUID getUniqueId() {
            return BukkitCommandSender.DEFAULT_ID;
         }
      };
   }
}
