package com.sk89q.worldedit.scripting;

import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Platform;

public abstract class CraftScriptEnvironment {
   protected WorldEdit controller;
   protected Player player;
   protected LocalConfiguration config;
   protected LocalSession session;
   protected Platform server;

   public CraftScriptEnvironment(WorldEdit controller, Platform server, LocalConfiguration config, LocalSession session, Player player) {
      this.controller = controller;
      this.player = player;
      this.config = config;
      this.server = server;
      this.session = session;
   }
}
