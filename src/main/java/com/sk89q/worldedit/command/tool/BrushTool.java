package com.sk89q.worldedit.command.tool;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.command.tool.brush.Brush;
import com.sk89q.worldedit.command.tool.brush.SphereBrush;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.MaskIntersection;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.session.request.Request;
import javax.annotation.Nullable;

public class BrushTool implements TraceTool {
   protected static int MAX_RANGE = 500;
   protected int range = -1;
   private Mask mask = null;
   private Brush brush = new SphereBrush();
   @Nullable
   private Pattern material;
   private double size = 1.0;
   private String permission;

   public BrushTool(String permission) {
      Preconditions.checkNotNull(permission);
      this.permission = permission;
   }

   @Override
   public boolean canUse(Actor player) {
      return player.hasPermission(this.permission);
   }

   public Mask getMask() {
      return this.mask;
   }

   public void setMask(Mask filter) {
      this.mask = filter;
   }

   public void setBrush(Brush brush, String permission) {
      this.brush = brush;
      this.permission = permission;
   }

   public Brush getBrush() {
      return this.brush;
   }

   public void setFill(@Nullable Pattern material) {
      this.material = material;
   }

   @Nullable
   public Pattern getMaterial() {
      return this.material;
   }

   public double getSize() {
      return this.size;
   }

   public void setSize(double radius) {
      this.size = radius;
   }

   public int getRange() {
      return this.range < 0 ? MAX_RANGE : Math.min(this.range, MAX_RANGE);
   }

   public void setRange(int range) {
      this.range = range;
   }

   @Override
   public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session) {
      WorldVector target = null;
      target = player.getBlockTrace(this.getRange(), true);
      if (target == null) {
         player.printError("No block in sight!");
         return true;
      } else {
         BlockBag bag = session.getBlockBag(player);
         EditSession editSession = session.createEditSession(player);
         Request.request().setEditSession(editSession);
         if (this.mask != null) {
            Mask existingMask = editSession.getMask();
            if (existingMask == null) {
               editSession.setMask(this.mask);
            } else if (existingMask instanceof MaskIntersection) {
               ((MaskIntersection)existingMask).add(this.mask);
            } else {
               MaskIntersection newMask = new MaskIntersection(existingMask);
               newMask.add(this.mask);
               editSession.setMask(newMask);
            }
         }

         try {
            this.brush.build(editSession, target, this.material, this.size);
         } catch (MaxChangedBlocksException var13) {
            player.printError("Max blocks change limit reached.");
         } finally {
            if (bag != null) {
               bag.flushChanges();
            }

            session.remember(editSession);
         }

         return true;
      }
   }
}
