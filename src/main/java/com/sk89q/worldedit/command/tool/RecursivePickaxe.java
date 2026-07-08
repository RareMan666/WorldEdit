package com.sk89q.worldedit.command.tool;

import com.sk89q.worldedit.BlockVector;
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
import java.util.HashSet;
import java.util.Set;

public class RecursivePickaxe implements BlockTool {
   private static final BaseBlock air = new BaseBlock(0);
   private double range;

   public RecursivePickaxe(double range) {
      this.range = range;
   }

   @Override
   public boolean canUse(Actor player) {
      return player.hasPermission("worldedit.superpickaxe.recursive");
   }

   @Override
   public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session, Location clicked) {
      World world = (World)clicked.getExtent();
      int initialType = world.getBlockType(clicked.toVector());
      if (initialType == 0) {
         return true;
      } else if (initialType == 7 && !player.canDestroyBedrock()) {
         return true;
      } else {
         EditSession editSession = session.createEditSession(player);
         editSession.getSurvivalExtent().setToolUse(config.superPickaxeManyDrop);

         try {
            recurse(server, editSession, world, clicked.toVector().toBlockVector(), clicked.toVector(), this.range, initialType, new HashSet<>());
         } catch (MaxChangedBlocksException var13) {
            player.printError("Max blocks change limit reached.");
         } finally {
            editSession.flushQueue();
            session.remember(editSession);
         }

         return true;
      }
   }

   private static void recurse(
      Platform server, EditSession editSession, World world, BlockVector pos, Vector origin, double size, int initialType, Set<BlockVector> visited
   ) throws MaxChangedBlocksException {
      double distanceSq = origin.distanceSq(pos);
      if (!(distanceSq > size * size) && !visited.contains(pos)) {
         visited.add(pos);
         if (editSession.getBlock(pos).getType() == initialType) {
            world.queueBlockBreakEffect(server, pos, initialType, distanceSq);
            editSession.setBlock(pos, air);
            recurse(server, editSession, world, pos.add(1, 0, 0).toBlockVector(), origin, size, initialType, visited);
            recurse(server, editSession, world, pos.add(-1, 0, 0).toBlockVector(), origin, size, initialType, visited);
            recurse(server, editSession, world, pos.add(0, 0, 1).toBlockVector(), origin, size, initialType, visited);
            recurse(server, editSession, world, pos.add(0, 0, -1).toBlockVector(), origin, size, initialType, visited);
            recurse(server, editSession, world, pos.add(0, 1, 0).toBlockVector(), origin, size, initialType, visited);
            recurse(server, editSession, world, pos.add(0, -1, 0).toBlockVector(), origin, size, initialType, visited);
         }
      }
   }
}
