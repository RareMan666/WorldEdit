package com.sk89q.worldedit.extension.input;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.world.World;
import javax.annotation.Nullable;

public class ParserContext {
   @Nullable
   private Extent extent;
   @Nullable
   private LocalSession session;
   @Nullable
   private World world;
   @Nullable
   private Actor actor;
   private boolean restricted = true;
   private boolean preferringWildcard;

   public ParserContext() {
   }

   public ParserContext(ParserContext other) {
      this.setExtent(other.getExtent());
      this.setSession(other.getSession());
      this.setWorld(other.getWorld());
      this.setActor(other.getActor());
      this.setRestricted(other.isRestricted());
      this.setPreferringWildcard(other.isPreferringWildcard());
   }

   @Nullable
   public Extent getExtent() {
      return this.extent;
   }

   public void setExtent(@Nullable Extent extent) {
      this.extent = extent;
   }

   @Nullable
   public LocalSession getSession() {
      return this.session;
   }

   public void setSession(@Nullable LocalSession session) {
      this.session = session;
   }

   @Nullable
   public World getWorld() {
      return this.world;
   }

   public void setWorld(@Nullable World world) {
      this.world = world;
      this.setExtent(world);
   }

   @Nullable
   public Actor getActor() {
      return this.actor;
   }

   public void setActor(@Nullable Actor actor) {
      this.actor = actor;
   }

   public Extent requireExtent() throws InputParseException {
      Extent extent = this.getExtent();
      if (extent == null) {
         throw new InputParseException("No Extent is known");
      } else {
         return extent;
      }
   }

   public LocalSession requireSession() throws InputParseException {
      LocalSession session = this.getSession();
      if (session == null) {
         throw new InputParseException("No LocalSession is known");
      } else {
         return session;
      }
   }

   public World requireWorld() throws InputParseException {
      World world = this.getWorld();
      if (world == null) {
         throw new InputParseException("No world is known");
      } else {
         return world;
      }
   }

   public Actor requireActor() throws InputParseException {
      Actor actor = this.getActor();
      if (actor == null) {
         throw new InputParseException("No actor is known");
      } else {
         return actor;
      }
   }

   public boolean isRestricted() {
      return this.restricted;
   }

   public void setRestricted(boolean restricted) {
      this.restricted = restricted;
   }

   public boolean isPreferringWildcard() {
      return this.preferringWildcard;
   }

   public void setPreferringWildcard(boolean preferringWildcard) {
      this.preferringWildcard = preferringWildcard;
   }
}
