package com.sk89q.worldedit.regions;

import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;

abstract class AbstractLegacyRegionSelector implements RegionSelector {
   @Deprecated
   public final void explainPrimarySelection(LocalPlayer player, LocalSession session, Vector position) {
      this.explainPrimarySelection(player, session, position);
   }

   @Deprecated
   public final void explainSecondarySelection(LocalPlayer player, LocalSession session, Vector position) {
      this.explainSecondarySelection(player, session, position);
   }

   @Deprecated
   public final void explainRegionAdjust(LocalPlayer player, LocalSession session) {
      this.explainRegionAdjust(player, session);
   }
}
