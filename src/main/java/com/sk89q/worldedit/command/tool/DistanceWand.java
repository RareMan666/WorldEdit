package com.sk89q.worldedit.command.tool;

import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extension.platform.permission.ActorSelectorLimits;
import com.sk89q.worldedit.regions.RegionSelector;

public class DistanceWand extends BrushTool implements DoubleActionTraceTool {
   public DistanceWand() {
      super("worldedit.wand");
   }

   @Override
   public boolean canUse(Actor player) {
      return player.hasPermission("worldedit.wand");
   }

   @Override
   public boolean actSecondary(Platform server, LocalConfiguration config, Player player, LocalSession session) {
      if (session.isToolControlEnabled() && player.hasPermission("worldedit.selection.pos")) {
         WorldVector target = this.getTarget(player);
         if (target == null) {
            return true;
         } else {
            RegionSelector selector = session.getRegionSelector(player.getWorld());
            if (selector.selectPrimary(target, ActorSelectorLimits.forActor(player))) {
               selector.explainPrimarySelection(player, session, target);
            }

            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session) {
      if (session.isToolControlEnabled() && player.hasPermission("worldedit.selection.pos")) {
         WorldVector target = this.getTarget(player);
         if (target == null) {
            return true;
         } else {
            RegionSelector selector = session.getRegionSelector(player.getWorld());
            if (selector.selectSecondary(target, ActorSelectorLimits.forActor(player))) {
               selector.explainSecondarySelection(player, session, target);
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public WorldVector getTarget(Player player) {
      WorldVector target = null;
      if (this.range > -1) {
         target = player.getBlockTrace(this.getRange(), true);
      } else {
         target = player.getBlockTrace(MAX_RANGE);
      }

      if (target == null) {
         player.printError("No block in sight!");
         return null;
      } else {
         return target;
      }
   }
}
