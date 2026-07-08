package com.sk89q.worldedit.bukkit;

import com.sk89q.worldedit.WorldEditException;

class WorldUnloadedException extends WorldEditException {
   WorldUnloadedException() {
      super("The world was unloaded already");
   }
}
