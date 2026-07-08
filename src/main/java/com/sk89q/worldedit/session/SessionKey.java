package com.sk89q.worldedit.session;

import com.sk89q.worldedit.util.Identifiable;
import javax.annotation.Nullable;

public interface SessionKey extends Identifiable {
   @Nullable
   String getName();

   boolean isActive();

   boolean isPersistent();
}
