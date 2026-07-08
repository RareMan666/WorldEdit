package com.sk89q.worldedit.extent.reorder;

import com.google.common.collect.Iterators;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.PlayerDirection;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.operation.BlockMapEntryPlacer;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.OperationQueue;
import com.sk89q.worldedit.function.operation.RunContext;
import com.sk89q.worldedit.util.collection.TupleArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MultiStageReorder extends AbstractDelegateExtent implements ReorderingExtent {
   private TupleArrayList<BlockVector, BaseBlock> stage1 = new TupleArrayList<>();
   private TupleArrayList<BlockVector, BaseBlock> stage2 = new TupleArrayList<>();
   private TupleArrayList<BlockVector, BaseBlock> stage3 = new TupleArrayList<>();
   private boolean enabled;

   public MultiStageReorder(Extent extent, boolean enabled) {
      super(extent);
      this.enabled = enabled;
   }

   public MultiStageReorder(Extent extent) {
      this(extent, true);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   @Override
   public boolean setBlock(Vector location, BaseBlock block) throws WorldEditException {
      BaseBlock lazyBlock = this.getLazyBlock(location);
      if (!this.enabled) {
         return super.setBlock(location, block);
      } else if (BlockType.shouldPlaceLast(block.getType())) {
         this.stage2.put(location.toBlockVector(), block);
         return lazyBlock.getType() != block.getType() || lazyBlock.getData() != block.getData();
      } else if (BlockType.shouldPlaceFinal(block.getType())) {
         this.stage3.put(location.toBlockVector(), block);
         return lazyBlock.getType() != block.getType() || lazyBlock.getData() != block.getData();
      } else if (BlockType.shouldPlaceLast(lazyBlock.getType())) {
         super.setBlock(location, new BaseBlock(0));
         return super.setBlock(location, block);
      } else {
         this.stage1.put(location.toBlockVector(), block);
         return lazyBlock.getType() != block.getType() || lazyBlock.getData() != block.getData();
      }
   }

   @Override
   public Operation commitBefore() {
      return new OperationQueue(
         new BlockMapEntryPlacer(this.getExtent(), Iterators.concat(this.stage1.iterator(), this.stage2.iterator())), new MultiStageReorder.Stage3Committer()
      );
   }

   private class Stage3Committer implements Operation {
      private Stage3Committer() {
      }

      @Override
      public Operation resume(RunContext run) throws WorldEditException {
         Extent extent = MultiStageReorder.this.getExtent();
         Set<BlockVector> blocks = new HashSet<>();
         Map<BlockVector, BaseBlock> blockTypes = new HashMap<>();

         for (Entry<BlockVector, BaseBlock> entry : MultiStageReorder.this.stage3) {
            BlockVector pt = entry.getKey();
            blocks.add(pt);
            blockTypes.put(pt, entry.getValue());
         }

         while (!blocks.isEmpty()) {
            BlockVector current = blocks.iterator().next();
            if (blocks.contains(current)) {
               Deque<BlockVector> walked = new LinkedList<>();

               do {
                  walked.addFirst(current);

                  assert blockTypes.containsKey(current);

                  BaseBlock baseBlock = blockTypes.get(current);
                  int type = baseBlock.getType();
                  int data = baseBlock.getData();
                  switch (type) {
                     case 27:
                     case 28:
                     case 66:
                     case 157:
                        BlockVector lowerBlock = current.add(0, -1, 0).toBlockVector();
                        if (blocks.contains(lowerBlock) && !walked.contains(lowerBlock)) {
                           walked.addFirst(lowerBlock);
                        }
                        break;
                     case 64:
                     case 71:
                     case 193:
                     case 194:
                     case 195:
                     case 196:
                     case 197:
                        if ((data & 8) == 0) {
                           BlockVector upperBlock = current.add(0, 1, 0).toBlockVector();
                           if (blocks.contains(upperBlock) && !walked.contains(upperBlock)) {
                              walked.addFirst(upperBlock);
                           }
                        }
                  }

                  PlayerDirection attachment = BlockType.getAttachment(type, data);
                  if (attachment == null) {
                     break;
                  }

                  current = current.add(attachment.vector()).toBlockVector();
               } while (blocks.contains(current) && !walked.contains(current));

               for (BlockVector pt : walked) {
                  extent.setBlock(pt, blockTypes.get(pt));
                  blocks.remove(pt);
               }
            }
         }

         MultiStageReorder.this.stage1.clear();
         MultiStageReorder.this.stage2.clear();
         MultiStageReorder.this.stage3.clear();
         return null;
      }

      @Override
      public void cancel() {
      }

      @Override
      public void addStatusMessages(List<String> messages) {
      }
   }
}
