package com.sk89q.worldedit.event.platform;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.event.AbstractCancellable;
import com.sk89q.worldedit.extension.platform.Actor;

public class CommandEvent extends AbstractCancellable {
   private final Actor actor;
   private final String arguments;

   public CommandEvent(Actor actor, String arguments) {
      Preconditions.checkNotNull(actor);
      Preconditions.checkNotNull(arguments);
      this.actor = actor;
      this.arguments = arguments;
   }

   public Actor getActor() {
      return this.actor;
   }

   public String getArguments() {
      return this.arguments;
   }
}
