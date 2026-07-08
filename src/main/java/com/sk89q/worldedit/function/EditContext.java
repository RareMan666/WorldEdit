package com.sk89q.worldedit.function;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.Region;
import javax.annotation.Nullable;

public class EditContext {
   private Extent destination;
   @Nullable
   private Region region;
   @Nullable
   private Pattern fill;

   public Extent getDestination() {
      return this.destination;
   }

   public void setDestination(Extent destination) {
      Preconditions.checkNotNull(destination, "destination");
      this.destination = destination;
   }

   @Nullable
   public Region getRegion() {
      return this.region;
   }

   public void setRegion(@Nullable Region region) {
      this.region = region;
   }

   @Nullable
   public Pattern getFill() {
      return this.fill;
   }

   public void setFill(@Nullable Pattern fill) {
      this.fill = fill;
   }
}
