package com.sk89q.worldedit.util.command;

import java.util.List;

public interface Description {
   List<Parameter> getParameters();

   String getDescription();

   String getHelp();

   String getUsage();

   List<String> getPermissions();
}
