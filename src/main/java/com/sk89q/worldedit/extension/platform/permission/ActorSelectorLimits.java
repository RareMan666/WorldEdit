package com.sk89q.worldedit.extension.platform.permission;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.regions.selector.limit.SelectorLimits;

public class ActorSelectorLimits implements SelectorLimits {
   private final LocalConfiguration configuration;
   private final Actor actor;

   public ActorSelectorLimits(LocalConfiguration configuration, Actor actor) {
      Preconditions.checkNotNull(configuration);
      Preconditions.checkNotNull(actor);
      this.configuration = configuration;
      this.actor = actor;
   }

   @Override
   public Optional<Integer> getPolygonVertexLimit() {
      int limit;
      if (this.actor.hasPermission("worldedit.limit.unrestricted") || this.configuration.maxPolygonalPoints < 0) {
         limit = this.configuration.defaultMaxPolygonalPoints;
      } else if (this.configuration.defaultMaxPolygonalPoints < 0) {
         limit = this.configuration.maxPolygonalPoints;
      } else {
         limit = Math.min(this.configuration.defaultMaxPolygonalPoints, this.configuration.maxPolygonalPoints);
      }

      return limit > 0 ? Optional.of(limit) : Optional.absent();
   }

   @Override
   public Optional<Integer> getPolyhedronVertexLimit() {
      int limit;
      if (this.actor.hasPermission("worldedit.limit.unrestricted") || this.configuration.maxPolyhedronPoints < 0) {
         limit = this.configuration.defaultMaxPolyhedronPoints;
      } else if (this.configuration.defaultMaxPolyhedronPoints < 0) {
         limit = this.configuration.maxPolyhedronPoints;
      } else {
         limit = Math.min(this.configuration.defaultMaxPolyhedronPoints, this.configuration.maxPolyhedronPoints);
      }

      return limit > 0 ? Optional.of(limit) : Optional.absent();
   }

   public static ActorSelectorLimits forActor(Actor actor) {
      return new ActorSelectorLimits(WorldEdit.getInstance().getConfiguration(), actor);
   }
}
