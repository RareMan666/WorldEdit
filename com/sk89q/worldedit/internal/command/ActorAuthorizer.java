package com.sk89q.worldedit.internal.command;

import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.util.auth.Authorizer;

public class ActorAuthorizer implements Authorizer {
   @Override
   public boolean testPermission(CommandLocals locals, String permission) {
      Actor sender = locals.get(Actor.class);
      if (sender == null) {
         throw new RuntimeException("Uh oh! No 'Actor' specified so that we can check permissions");
      } else {
         return sender.hasPermission(permission);
      }
   }
}
