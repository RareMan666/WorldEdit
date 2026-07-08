package com.sk89q.bukkit.util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface CommandInspector {
   String getShortText(Command var1);

   String getFullText(Command var1);

   boolean testPermission(CommandSender var1, Command var2);
}
