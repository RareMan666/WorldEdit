package com.sk89q.worldedit.event;

public interface Cancellable {
   boolean isCancelled();

   void setCancelled(boolean var1);
}
