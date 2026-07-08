package com.sk89q.worldedit.internal.command;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Capability;
import com.sk89q.worldedit.extension.platform.MultiUserPlatform;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extension.platform.PlatformManager;
import com.sk89q.worldedit.util.command.CommandCompleter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserCommandCompleter implements CommandCompleter {
   private final PlatformManager platformManager;

   public UserCommandCompleter(PlatformManager platformManager) {
      Preconditions.checkNotNull(platformManager);
      this.platformManager = platformManager;
   }

   @Override
   public List<String> getSuggestions(String arguments, CommandLocals locals) throws CommandException {
      Platform platform = this.platformManager.queryCapability(Capability.USER_COMMANDS);
      if (platform instanceof MultiUserPlatform) {
         List<String> suggestions = new ArrayList<>();

         for (Actor user : ((MultiUserPlatform)platform).getConnectedUsers()) {
            if (user.getName().toLowerCase().startsWith(arguments.toLowerCase().trim())) {
               suggestions.add(user.getName());
            }
         }

         return suggestions;
      } else {
         return Collections.emptyList();
      }
   }
}
