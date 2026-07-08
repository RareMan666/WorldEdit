package com.sk89q.worldedit.extension.platform;

import com.sk89q.worldedit.world.World;
import java.util.Collections;
import java.util.List;

public abstract class AbstractPlatform implements Platform {
   @Override
   public int schedule(long delay, long period, Runnable task) {
      return -1;
   }

   @Override
   public List<? extends World> getWorlds() {
      return Collections.emptyList();
   }
}
