package com.sk89q.worldedit.internal.cui;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.extension.platform.Actor;

public interface CUIRegion {
   void describeCUI(LocalSession var1, Actor var2);

   void describeLegacyCUI(LocalSession var1, Actor var2);

   int getProtocolVersion();

   String getTypeID();

   String getLegacyTypeID();
}
