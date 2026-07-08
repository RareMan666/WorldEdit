package com.sk89q.worldedit.util.auth;

import com.sk89q.minecraft.util.commands.CommandLocals;

public interface Authorizer {
   boolean testPermission(CommandLocals var1, String var2);
}
