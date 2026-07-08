package com.sk89q.bukkit.util;

import com.sk89q.minecraft.util.commands.CommandsManager;
import com.sk89q.util.StringUtil;
import com.sk89q.wepif.PermissionsResolverManager;
import java.util.Arrays;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

public class DynamicPluginCommand extends Command implements PluginIdentifiableCommand {
   protected final CommandExecutor owner;
   protected final Object registeredWith;
   protected final Plugin owningPlugin;
   protected String[] permissions = new String[0];

   public DynamicPluginCommand(String[] aliases, String desc, String usage, CommandExecutor owner, Object registeredWith, Plugin plugin) {
      super(aliases[0], desc, usage, Arrays.asList(aliases));
      this.owner = owner;
      this.owningPlugin = plugin;
      this.registeredWith = registeredWith;
   }

   public boolean execute(CommandSender sender, String label, String[] args) {
      return this.owner.onCommand(sender, this, label, args);
   }

   public Object getOwner() {
      return this.owner;
   }

   public Object getRegisteredWith() {
      return this.registeredWith;
   }

   public void setPermissions(String[] permissions) {
      this.permissions = permissions;
      if (permissions != null) {
         super.setPermission(StringUtil.joinString(permissions, ";"));
      }
   }

   public String[] getPermissions() {
      return this.permissions;
   }

   public Plugin getPlugin() {
      return this.owningPlugin;
   }

   public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
      return this.registeredWith instanceof CommandInspector
         ? ((TabCompleter)this.owner).onTabComplete(sender, this, alias, args)
         : super.tabComplete(sender, alias, args);
   }

   public boolean testPermissionSilent(CommandSender sender) {
      if (this.permissions == null || this.permissions.length == 0) {
         return true;
      } else if (this.registeredWith instanceof CommandInspector) {
         CommandInspector resolver = (CommandInspector)this.registeredWith;
         return resolver.testPermission(sender, this);
      } else {
         if (this.registeredWith instanceof CommandsManager) {
            try {
               for (String permission : this.permissions) {
                  if (((CommandsManager)this.registeredWith).hasPermission(sender, permission)) {
                     return true;
                  }
               }

               return false;
            } catch (Throwable var6) {
            }
         } else if (PermissionsResolverManager.isInitialized() && sender instanceof OfflinePlayer) {
            for (String permissionx : this.permissions) {
               if (PermissionsResolverManager.getInstance().hasPermission((OfflinePlayer)sender, permissionx)) {
                  return true;
               }
            }

            return false;
         }

         return super.testPermissionSilent(sender);
      }
   }
}
