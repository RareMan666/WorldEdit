package com.sk89q.worldedit.command.tool;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;

public class BlockReplacer implements DoubleActionBlockTool {
   private BaseBlock targetBlock;

   public BlockReplacer(BaseBlock targetBlock) {
      this.targetBlock = targetBlock;
   }

   @Override
   public boolean canUse(Actor player) {
      return player.hasPermission("worldedit.tool.replacer");
   }

   @Override
   public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session, Location clicked) {
      BlockBag bag = session.getBlockBag(player);
      EditSession editSession = session.createEditSession(player);

      try {
         editSession.setBlock(clicked.toVector(), this.targetBlock);
      } catch (MaxChangedBlocksException var12) {
      } finally {
         if (bag != null) {
            bag.flushChanges();
         }

         session.remember(editSession);
      }

      return true;
   }

   @Override
   public boolean actSecondary(Platform server, LocalConfiguration config, Player player, LocalSession session, Location clicked) {
      World world = (World)clicked.getExtent();
      EditSession editSession = session.createEditSession(player);
      this.targetBlock = editSession.getBlock(clicked.toVector());
      BlockType type = BlockType.fromID(this.targetBlock.getType());
      if (type != null) {
         player.print("Replacer tool switched to: " + type.getName());
      }

      return true;
   }
}
