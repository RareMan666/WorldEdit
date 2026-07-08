package com.sk89q.worldedit.util;

import com.sk89q.worldedit.BlockWorldVector;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVectorFace;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.internal.LocalWorldAdapter;

public class TargetBlock {
   private LocalWorld world;
   private int maxDistance;
   private double checkDistance;
   private double curDistance;
   private Vector targetPos = new Vector();
   private Vector targetPosDouble = new Vector();
   private Vector prevPos = new Vector();
   private Vector offset = new Vector();

   public TargetBlock(LocalPlayer player) {
      this.world = LocalWorldAdapter.adapt(player.getWorld());
      this.setValues(player.getPosition(), player.getYaw(), player.getPitch(), 300, 1.65, 0.2);
   }

   public TargetBlock(LocalPlayer player, int maxDistance, double checkDistance) {
      this((Player)player, maxDistance, checkDistance);
   }

   public TargetBlock(Player player, int maxDistance, double checkDistance) {
      this.world = LocalWorldAdapter.adapt(player.getWorld());
      this.setValues(player.getPosition(), player.getYaw(), player.getPitch(), maxDistance, 1.65, checkDistance);
   }

   private void setValues(Vector loc, double xRotation, double yRotation, int maxDistance, double viewHeight, double checkDistance) {
      this.maxDistance = maxDistance;
      this.checkDistance = checkDistance;
      this.curDistance = 0.0;
      xRotation = (xRotation + 90.0) % 360.0;
      yRotation *= -1.0;
      double h = checkDistance * Math.cos(Math.toRadians(yRotation));
      this.offset = new Vector(
         h * Math.cos(Math.toRadians(xRotation)), checkDistance * Math.sin(Math.toRadians(yRotation)), h * Math.sin(Math.toRadians(xRotation))
      );
      this.targetPosDouble = loc.add(0.0, viewHeight, 0.0);
      this.targetPos = this.targetPosDouble.toBlockPoint();
      this.prevPos = this.targetPos;
   }

   public BlockWorldVector getAnyTargetBlock() {
      boolean searchForLastBlock = true;
      BlockWorldVector lastBlock = null;

      while (this.getNextBlock() != null && this.world.getBlockType(this.getCurrentBlock()) == 0) {
         if (searchForLastBlock) {
            lastBlock = this.getCurrentBlock();
            if (lastBlock.getBlockY() <= 0 || lastBlock.getBlockY() >= this.world.getMaxY()) {
               searchForLastBlock = false;
            }
         }
      }

      BlockWorldVector currentBlock = this.getCurrentBlock();
      return currentBlock != null ? currentBlock : lastBlock;
   }

   public BlockWorldVector getTargetBlock() {
      while (this.getNextBlock() != null && this.world.getBlockType(this.getCurrentBlock()) == 0) {
      }

      return this.getCurrentBlock();
   }

   public BlockWorldVector getSolidTargetBlock() {
      while (this.getNextBlock() != null && BlockType.canPassThrough(this.world.getBlock(this.getCurrentBlock()))) {
      }

      return this.getCurrentBlock();
   }

   public BlockWorldVector getNextBlock() {
      this.prevPos = this.targetPos;

      do {
         this.curDistance = this.curDistance + this.checkDistance;
         this.targetPosDouble = this.offset.add(this.targetPosDouble.getX(), this.targetPosDouble.getY(), this.targetPosDouble.getZ());
         this.targetPos = this.targetPosDouble.toBlockPoint();
      } while (
         this.curDistance <= this.maxDistance
            && this.targetPos.getBlockX() == this.prevPos.getBlockX()
            && this.targetPos.getBlockY() == this.prevPos.getBlockY()
            && this.targetPos.getBlockZ() == this.prevPos.getBlockZ()
      );

      return this.curDistance > this.maxDistance ? null : new BlockWorldVector(this.world, this.targetPos);
   }

   public BlockWorldVector getCurrentBlock() {
      return this.curDistance > this.maxDistance ? null : new BlockWorldVector(this.world, this.targetPos);
   }

   public BlockWorldVector getPreviousBlock() {
      return new BlockWorldVector(this.world, this.prevPos);
   }

   public WorldVectorFace getAnyTargetBlockFace() {
      this.getAnyTargetBlock();
      return WorldVectorFace.getWorldVectorFace(this.world, this.getCurrentBlock(), this.getPreviousBlock());
   }

   public WorldVectorFace getTargetBlockFace() {
      this.getAnyTargetBlock();
      return WorldVectorFace.getWorldVectorFace(this.world, this.getCurrentBlock(), this.getPreviousBlock());
   }
}
