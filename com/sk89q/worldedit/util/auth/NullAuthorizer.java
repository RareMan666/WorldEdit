package com.sk89q.worldedit.util.auth;

import com.sk89q.minecraft.util.commands.CommandLocals;

public class NullAuthorizer implements Authorizer {
   @Override
   public boolean testPermission(CommandLocals locals, String permission) {
      return false;
   }
}
