package com.sk89q.worldedit.command.tool;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldVectorFace;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;

public class LongRangeBuildTool extends BrushTool implements DoubleActionTraceTool {
   BaseBlock primary;
   BaseBlock secondary;

   public LongRangeBuildTool(BaseBlock primary, BaseBlock secondary) {
      super("worldedit.tool.lrbuild");
      this.primary = primary;
      this.secondary = secondary;
   }

   @Override
   public boolean canUse(Actor player) {
      return player.hasPermission("worldedit.tool.lrbuild");
   }

   @Override
   public boolean actSecondary(Platform server, LocalConfiguration config, Player player, LocalSession session) {
      WorldVectorFace pos = this.getTargetFace(player);
      if (pos == null) {
         return false;
      } else {
         EditSession eS = session.createEditSession(player);

         try {
            if (this.secondary.getType() == 0) {
               eS.setBlock(pos, this.secondary);
            } else {
               eS.setBlock(pos.getFaceVector(), this.secondary);
            }

            return true;
         } catch (MaxChangedBlocksException var8) {
            return false;
         }
      }
   }

   @Override
   public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session) {
      WorldVectorFace pos = this.getTargetFace(player);
      if (pos == null) {
         return false;
      } else {
         EditSession eS = session.createEditSession(player);

         try {
            if (this.primary.getType() == 0) {
               eS.setBlock(pos, this.primary);
            } else {
               eS.setBlock(pos.getFaceVector(), this.primary);
            }

            return true;
         } catch (MaxChangedBlocksException var8) {
            return false;
         }
      }
   }

   public WorldVectorFace getTargetFace(Player player) {
      WorldVectorFace target = null;
      target = player.getBlockTraceFace(this.getRange(), true);
      if (target == null) {
         player.printError("No block in sight!");
         return null;
      } else {
         return target;
      }
   }
}
