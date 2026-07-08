package com.sk89q.worldedit.command.tool;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;

public class AreaPickaxe implements BlockTool {
   private static final BaseBlock air = new BaseBlock(0);
   private int range;

   public AreaPickaxe(int range) {
      this.range = range;
   }

   @Override
   public boolean canUse(Actor player) {
      return player.hasPermission("worldedit.superpickaxe.area");
   }

   @Override
   public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session, Location clicked) {
      int ox = clicked.getBlockX();
      int oy = clicked.getBlockY();
      int oz = clicked.getBlockZ();
      int initialType = ((World)clicked.getExtent()).getBlockType(clicked.toVector());
      if (initialType == 0) {
         return true;
      } else if (initialType == 7 && !player.canDestroyBedrock()) {
         return true;
      } else {
         EditSession editSession = session.createEditSession(player);
         editSession.getSurvivalExtent().setToolUse(config.superPickaxeManyDrop);

         try {
            for (int x = ox - this.range; x <= ox + this.range; x++) {
               for (int y = oy - this.range; y <= oy + this.range; y++) {
                  for (int z = oz - this.range; z <= oz + this.range; z++) {
                     Vector pos = new Vector(x, y, z);
                     if (editSession.getBlockType(pos) == initialType) {
                        ((World)clicked.getExtent()).queueBlockBreakEffect(server, pos, initialType, clicked.toVector().distanceSq(pos));
                        editSession.setBlock(pos, air);
                     }
                  }
               }
            }
         } catch (MaxChangedBlocksException var18) {
            player.printError("Max blocks change limit reached.");
         } finally {
            editSession.flushQueue();
            session.remember(editSession);
         }

         return true;
      }
   }
}
