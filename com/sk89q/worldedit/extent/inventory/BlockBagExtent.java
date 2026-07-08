package com.sk89q.worldedit.extent.inventory;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extent.AbstractDelegateExtent;
import com.sk89q.worldedit.extent.Extent;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

public class BlockBagExtent extends AbstractDelegateExtent {
   private Map<Integer, Integer> missingBlocks = new HashMap<>();
   private BlockBag blockBag;

   public BlockBagExtent(Extent extent, @Nullable BlockBag blockBag) {
      super(extent);
      this.blockBag = blockBag;
   }

   @Nullable
   public BlockBag getBlockBag() {
      return this.blockBag;
   }

   public void setBlockBag(@Nullable BlockBag blockBag) {
      this.blockBag = blockBag;
   }

   public Map<Integer, Integer> popMissing() {
      Map<Integer, Integer> missingBlocks = this.missingBlocks;
      this.missingBlocks = new HashMap<>();
      return missingBlocks;
   }

   @Override
   public boolean setBlock(Vector position, BaseBlock block) throws WorldEditException {
      if (this.blockBag != null) {
         BaseBlock lazyBlock = this.getExtent().getLazyBlock(position);
         int existing = lazyBlock.getType();
         int type = block.getType();
         if (type > 0) {
            try {
               this.blockBag.fetchPlacedBlock(type, 0);
            } catch (UnplaceableBlockException var8) {
               return false;
            } catch (BlockBagException var9) {
               if (!this.missingBlocks.containsKey(type)) {
                  this.missingBlocks.put(type, 1);
               } else {
                  this.missingBlocks.put(type, this.missingBlocks.get(type) + 1);
               }

               return false;
            }
         }

         if (existing > 0) {
            try {
               this.blockBag.storeDroppedBlock(existing, lazyBlock.getData());
            } catch (BlockBagException var7) {
            }
         }
      }

      return super.setBlock(position, block);
   }
}
