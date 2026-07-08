package com.sk89q.worldedit.event.platform;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.event.Cancellable;
import com.sk89q.worldedit.event.Event;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.util.Location;

public class BlockInteractEvent extends Event implements Cancellable {
   private final Actor cause;
   private final Location location;
   private final Interaction type;
   private boolean cancelled;

   public BlockInteractEvent(Actor cause, Location location, Interaction type) {
      Preconditions.checkNotNull(cause);
      Preconditions.checkNotNull(location);
      Preconditions.checkNotNull(type);
      this.cause = cause;
      this.location = location;
      this.type = type;
   }

   public Actor getCause() {
      return this.cause;
   }

   public Location getLocation() {
      return this.location;
   }

   public Interaction getType() {
      return this.type;
   }

   @Override
   public boolean isCancelled() {
      return this.cancelled;
   }

   @Override
   public void setCancelled(boolean cancelled) {
      this.cancelled = cancelled;
   }
}
