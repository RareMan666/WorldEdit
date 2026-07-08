package com.sk89q.worldedit.event.extent;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.event.Event;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.world.World;
import javax.annotation.Nullable;

public class EditSessionEvent extends Event {
   private final World world;
   private final Actor actor;
   private final int maxBlocks;
   private final EditSession.Stage stage;
   private Extent extent;

   public EditSessionEvent(@Nullable World world, Actor actor, int maxBlocks, EditSession.Stage stage) {
      this.world = world;
      this.actor = actor;
      this.maxBlocks = maxBlocks;
      this.stage = stage;
   }

   @Nullable
   public Actor getActor() {
      return this.actor;
   }

   @Nullable
   public World getWorld() {
      return this.world;
   }

   public int getMaxBlocks() {
      return this.maxBlocks;
   }

   public Extent getExtent() {
      return this.extent;
   }

   public EditSession.Stage getStage() {
      return this.stage;
   }

   public void setExtent(Extent extent) {
      Preconditions.checkNotNull(extent);
      this.extent = extent;
   }

   public EditSessionEvent clone(EditSession.Stage stage) {
      return new EditSessionEvent(this.world, this.actor, this.maxBlocks, stage);
   }
}
