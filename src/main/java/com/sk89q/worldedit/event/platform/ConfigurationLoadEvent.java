package com.sk89q.worldedit.event.platform;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.event.Event;

public class ConfigurationLoadEvent extends Event {
   private final LocalConfiguration configuration;

   public ConfigurationLoadEvent(LocalConfiguration configuration) {
      Preconditions.checkNotNull(configuration);
      this.configuration = configuration;
   }

   public LocalConfiguration getConfiguration() {
      return this.configuration;
   }
}
