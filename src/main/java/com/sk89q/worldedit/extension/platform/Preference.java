package com.sk89q.worldedit.extension.platform;

import com.google.common.base.Preconditions;

public enum Preference {
   PREFERRED,
   NORMAL,
   PREFER_OTHERS;

   public boolean isPreferredOver(Preference other) {
      Preconditions.checkNotNull(other);
      return this.ordinal() < other.ordinal();
   }
}
