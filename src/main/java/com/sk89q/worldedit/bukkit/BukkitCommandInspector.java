package com.sk89q.worldedit.bukkit;

import com.google.common.base.Preconditions;
import com.sk89q.bukkit.util.CommandInspector;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.util.command.CommandMapping;
import com.sk89q.worldedit.util.command.Description;
import com.sk89q.worldedit.util.command.Dispatcher;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

class BukkitCommandInspector implements CommandInspector {
   private static final Logger logger = Logger.getLogger(BukkitCommandInspector.class.getCanonicalName());
   private final WorldEditPlugin plugin;
   private final Dispatcher dispatcher;

   BukkitCommandInspector(WorldEditPlugin plugin, Dispatcher dispatcher) {
      Preconditions.checkNotNull(plugin);
      Preconditions.checkNotNull(dispatcher);
      this.plugin = plugin;
      this.dispatcher = dispatcher;
   }

   @Override
   public String getShortText(Command command) {
      CommandMapping mapping = this.dispatcher.get(command.getName());
      if (mapping != null) {
         return mapping.getDescription().getDescription();
      } else {
         logger.warning("BukkitCommandInspector doesn't know how about the command '" + command + "'");
         return "Help text not available";
      }
   }

   @Override
   public String getFullText(Command command) {
      CommandMapping mapping = this.dispatcher.get(command.getName());
      if (mapping != null) {
         Description description = mapping.getDescription();
         return "Usage: " + description.getUsage() + (description.getHelp() != null ? "\n" + description.getHelp() : "");
      } else {
         logger.warning("BukkitCommandInspector doesn't know how about the command '" + command + "'");
         return "Help text not available";
      }
   }

   @Override
   public boolean testPermission(CommandSender sender, Command command) {
      CommandMapping mapping = this.dispatcher.get(command.getName());
      if (mapping != null) {
         CommandLocals locals = new CommandLocals();
         locals.put(Actor.class, this.plugin.wrapCommandSender(sender));
         return mapping.getCallable().testPermission(locals);
      } else {
         logger.warning("BukkitCommandInspector doesn't know how about the command '" + command + "'");
         return false;
      }
   }
}
