package com.sk89q.worldedit.world;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BaseItem;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.function.mask.BlockMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.TreeGenerator;
import java.util.PriorityQueue;
import javax.annotation.Nullable;

public abstract class AbstractWorld implements World {
   private final PriorityQueue<AbstractWorld.QueuedEffect> effectQueue = new PriorityQueue<>();
   private int taskId = -1;

   @Override
   public boolean useItem(Vector position, BaseItem item, Direction face) {
      return false;
   }

   @Override
   public final boolean setBlockType(Vector position, int type) {
      try {
         return this.setBlock(position, new BaseBlock(type));
      } catch (WorldEditException var4) {
         return false;
      }
   }

   @Override
   public final void setBlockData(Vector position, int data) {
      try {
         this.setBlock(position, new BaseBlock(this.getLazyBlock(position).getType(), data));
      } catch (WorldEditException var4) {
      }
   }

   @Override
   public final boolean setTypeIdAndData(Vector position, int type, int data) {
      try {
         return this.setBlock(position, new BaseBlock(type, data));
      } catch (WorldEditException var5) {
         return false;
      }
   }

   @Override
   public final boolean setBlock(Vector pt, BaseBlock block) throws WorldEditException {
      return this.setBlock(pt, block, true);
   }

   @Override
   public int getMaxY() {
      return this.getMaximumPoint().getBlockY();
   }

   @Override
   public boolean isValidBlockType(int type) {
      return BlockType.fromID(type) != null;
   }

   @Override
   public boolean usesBlockData(int type) {
      return BlockType.usesData(type) || BlockType.fromID(type) == null;
   }

   @Override
   public Mask createLiquidMask() {
      return new BlockMask(this, new BaseBlock(11, -1), new BaseBlock(10, -1), new BaseBlock(9, -1), new BaseBlock(8, -1));
   }

   @Override
   public int getBlockType(Vector pt) {
      return this.getLazyBlock(pt).getType();
   }

   @Override
   public int getBlockData(Vector pt) {
      return this.getLazyBlock(pt).getData();
   }

   @Override
   public void dropItem(Vector pt, BaseItemStack item, int times) {
      for (int i = 0; i < times; i++) {
         this.dropItem(pt, item);
      }
   }

   @Override
   public void simulateBlockMine(Vector pt) {
      BaseBlock block = this.getLazyBlock(pt);
      BaseItemStack stack = BlockType.getBlockDrop(block.getId(), (short)block.getData());
      if (stack != null) {
         int amount = stack.getAmount();
         if (amount > 1) {
            this.dropItem(pt, new BaseItemStack(stack.getType(), 1, stack.getData()), amount);
         } else {
            this.dropItem(pt, stack, amount);
         }
      }

      try {
         this.setBlock(pt, new BaseBlock(0));
      } catch (WorldEditException var5) {
         throw new RuntimeException(var5);
      }
   }

   @Override
   public boolean generateTree(EditSession editSession, Vector pt) throws MaxChangedBlocksException {
      return this.generateTree(TreeGenerator.TreeType.TREE, editSession, pt);
   }

   @Override
   public boolean generateBigTree(EditSession editSession, Vector pt) throws MaxChangedBlocksException {
      return this.generateTree(TreeGenerator.TreeType.BIG_TREE, editSession, pt);
   }

   @Override
   public boolean generateBirchTree(EditSession editSession, Vector pt) throws MaxChangedBlocksException {
      return this.generateTree(TreeGenerator.TreeType.BIRCH, editSession, pt);
   }

   @Override
   public boolean generateRedwoodTree(EditSession editSession, Vector pt) throws MaxChangedBlocksException {
      return this.generateTree(TreeGenerator.TreeType.REDWOOD, editSession, pt);
   }

   @Override
   public boolean generateTallRedwoodTree(EditSession editSession, Vector pt) throws MaxChangedBlocksException {
      return this.generateTree(TreeGenerator.TreeType.TALL_REDWOOD, editSession, pt);
   }

   @Override
   public void checkLoadedChunk(Vector pt) {
   }

   @Override
   public void fixAfterFastMode(Iterable<BlockVector2D> chunks) {
   }

   @Override
   public void fixLighting(Iterable<BlockVector2D> chunks) {
   }

   @Override
   public boolean playEffect(Vector position, int type, int data) {
      return false;
   }

   @Override
   public boolean queueBlockBreakEffect(Platform server, Vector position, int blockId, double priority) {
      if (this.taskId == -1) {
         this.taskId = server.schedule(0L, 1L, new Runnable() {
            @Override
            public void run() {
               int max = Math.max(1, Math.min(30, AbstractWorld.this.effectQueue.size() / 3));

               for (int i = 0; i < max; i++) {
                  if (AbstractWorld.this.effectQueue.isEmpty()) {
                     return;
                  }

                  AbstractWorld.this.effectQueue.poll().play();
               }
            }
         });
      }

      if (this.taskId == -1) {
         return false;
      } else {
         this.effectQueue.offer(new AbstractWorld.QueuedEffect(position, blockId, priority));
         return true;
      }
   }

   @Override
   public Vector getMinimumPoint() {
      return new Vector(-30000000, 0, -30000000);
   }

   @Override
   public Vector getMaximumPoint() {
      return new Vector(30000000, 255, 30000000);
   }

   @Nullable
   @Override
   public Operation commit() {
      return null;
   }

   private class QueuedEffect implements Comparable<AbstractWorld.QueuedEffect> {
      private final Vector position;
      private final int blockId;
      private final double priority;

      private QueuedEffect(Vector position, int blockId, double priority) {
         this.position = position;
         this.blockId = blockId;
         this.priority = priority;
      }

      public void play() {
         AbstractWorld.this.playEffect(this.position, 2001, this.blockId);
      }

      public int compareTo(@Nullable AbstractWorld.QueuedEffect other) {
         return Double.compare(this.priority, other != null ? other.priority : 0.0);
      }
   }
}
