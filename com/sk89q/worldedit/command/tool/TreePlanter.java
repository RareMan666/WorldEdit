package com.sk89q.worldedit.command.tool;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.util.TreeGenerator;

public class TreePlanter implements BlockTool {
   private TreeGenerator gen;

   public TreePlanter(TreeGenerator gen) {
      this.gen = gen;
   }

   @Override
   public boolean canUse(Actor player) {
      return player.hasPermission("worldedit.tool.tree");
   }

   @Override
   public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session, Location clicked) {
      EditSession editSession = session.createEditSession(player);

      try {
         boolean successful = false;

         for (int i = 0; i < 10; i++) {
            if (this.gen.generate(editSession, clicked.toVector().add(0, 1, 0))) {
               successful = true;
               break;
            }
         }

         if (!successful) {
            player.printError("A tree can't go there.");
         }
      } catch (MaxChangedBlocksException var12) {
         player.printError("Max. blocks changed reached.");
      } finally {
         session.remember(editSession);
      }

      return true;
   }
}
