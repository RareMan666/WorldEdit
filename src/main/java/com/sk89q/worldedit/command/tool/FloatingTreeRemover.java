package com.sk89q.worldedit.command.tool;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.PlayerDirection;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class FloatingTreeRemover implements BlockTool {
   private static final BaseBlock AIR = new BaseBlock(0);
   private int rangeSq;
   Vector[] recurseDirections = new Vector[]{
      PlayerDirection.NORTH.vector(),
      PlayerDirection.EAST.vector(),
      PlayerDirection.SOUTH.vector(),
      PlayerDirection.WEST.vector(),
      PlayerDirection.UP.vector(),
      PlayerDirection.DOWN.vector()
   };

   public FloatingTreeRemover() {
      this.rangeSq = 10000;
   }

   @Override
   public boolean canUse(Actor player) {
      return player.hasPermission("worldedit.tool.deltree");
   }

   @Override
   public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session, Location clicked) {
      World world = (World)clicked.getExtent();
      switch (world.getBlockType(clicked.toVector())) {
         case 17:
         case 18:
         case 99:
         case 100:
         case 106:
         case 161:
         case 162:
            EditSession editSession = session.createEditSession(player);

            boolean var9;
            try {
               Set<Vector> blockSet = this.bfs(world, clicked.toVector());
               if (blockSet != null) {
                  for (Vector blockVector : blockSet) {
                     int typeId = editSession.getBlock(blockVector).getType();
                     switch (typeId) {
                        case 17:
                        case 18:
                        case 99:
                        case 100:
                        case 106:
                        case 161:
                        case 162:
                           editSession.setBlock(blockVector, AIR);
                     }
                  }

                  return true;
               }

               player.printError("That's not a floating tree.");
               var9 = true;
            } catch (MaxChangedBlocksException var15) {
               player.printError("Max blocks change limit reached.");
               return true;
            } finally {
               session.remember(editSession);
            }

            return var9;
         default:
            player.printError("That's not a tree.");
            return true;
      }
   }

   private Set<Vector> bfs(World world, Vector origin) throws MaxChangedBlocksException {
      Set<Vector> visited = new HashSet<>();
      LinkedList<Vector> queue = new LinkedList<>();
      queue.addLast(origin);
      visited.add(origin);

      while (!queue.isEmpty()) {
         Vector current = queue.removeFirst();

         for (Vector recurseDirection : this.recurseDirections) {
            Vector next = current.add(recurseDirection);
            if (!(origin.distanceSq(next) > this.rangeSq) && visited.add(next)) {
               switch (world.getBlockType(next)) {
                  case 0:
                  case 78:
                     break;
                  case 17:
                  case 18:
                  case 99:
                  case 100:
                  case 106:
                  case 161:
                  case 162:
                     queue.addLast(next);
                     break;
                  default:
                     int curId = world.getBlockType(current);
                     if (curId != 18 && curId != 161 && curId != 106) {
                        return null;
                     }
               }
            }
         }
      }

      return visited;
   }
}
