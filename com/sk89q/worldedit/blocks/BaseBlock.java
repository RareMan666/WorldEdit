package com.sk89q.worldedit.blocks;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.foundation.Block;
import java.util.Collection;
import javax.annotation.Nullable;

public class BaseBlock extends Block implements TileEntityBlock {
   public static final int MAX_ID = 4095;
   public static final int MAX_DATA = 15;
   private short id;
   private short data;
   @Nullable
   private CompoundTag nbtData;

   public BaseBlock(int id) {
      this.internalSetId(id);
      this.internalSetData(0);
   }

   public BaseBlock(int id, int data) {
      this.internalSetId(id);
      this.internalSetData(data);
   }

   public BaseBlock(int id, int data, @Nullable CompoundTag nbtData) {
      this.setId(id);
      this.setData(data);
      this.setNbtData(nbtData);
   }

   public BaseBlock(BaseBlock other) {
      this(other.getId(), other.getData(), other.getNbtData());
   }

   @Override
   public int getId() {
      return this.id;
   }

   protected final void internalSetId(int id) {
      if (id > 4095) {
         throw new IllegalArgumentException("Can't have a block ID above 4095 (" + id + " given)");
      } else if (id < 0) {
         throw new IllegalArgumentException("Can't have a block ID below 0");
      } else {
         this.id = (short)id;
      }
   }

   @Override
   public void setId(int id) {
      this.internalSetId(id);
   }

   @Override
   public int getData() {
      return this.data;
   }

   protected final void internalSetData(int data) {
      if (data > 15) {
         throw new IllegalArgumentException("Can't have a block data value above 15 (" + data + " given)");
      } else if (data < -1) {
         throw new IllegalArgumentException("Can't have a block data value below -1");
      } else {
         this.data = (short)data;
      }
   }

   @Override
   public void setData(int data) {
      this.internalSetData(data);
   }

   @Override
   public void setIdAndData(int id, int data) {
      this.setId(id);
      this.setData(data);
   }

   @Override
   public boolean hasWildcardData() {
      return this.getData() == -1;
   }

   @Override
   public boolean hasNbtData() {
      return this.getNbtData() != null;
   }

   @Override
   public String getNbtId() {
      CompoundTag nbtData = this.getNbtData();
      if (nbtData == null) {
         return "";
      } else {
         Tag idTag = nbtData.getValue().get("id");
         return idTag != null && idTag instanceof StringTag ? ((StringTag)idTag).getValue() : "";
      }
   }

   @Nullable
   @Override
   public CompoundTag getNbtData() {
      return this.nbtData;
   }

   @Override
   public void setNbtData(@Nullable CompoundTag nbtData) {
      this.nbtData = nbtData;
   }

   public int getType() {
      return this.getId();
   }

   public void setType(int type) {
      this.setId(type);
   }

   public boolean isAir() {
      return this.getType() == 0;
   }

   @Deprecated
   public int rotate90() {
      int newData = BlockData.rotate90(this.getType(), this.getData());
      this.setData(newData);
      return newData;
   }

   @Deprecated
   public int rotate90Reverse() {
      int newData = BlockData.rotate90Reverse(this.getType(), this.getData());
      this.setData((short)newData);
      return newData;
   }

   @Deprecated
   public int cycleData(int increment) {
      int newData = BlockData.cycle(this.getType(), this.getData(), increment);
      this.setData((short)newData);
      return newData;
   }

   @Deprecated
   public BaseBlock flip() {
      this.setData((short)BlockData.flip(this.getType(), this.getData()));
      return this;
   }

   @Deprecated
   public BaseBlock flip(CuboidClipboard.FlipDirection direction) {
      this.setData((short)BlockData.flip(this.getType(), this.getData(), direction));
      return this;
   }

   @Override
   public boolean equals(Object o) {
      if (!(o instanceof BaseBlock)) {
         return false;
      } else {
         BaseBlock otherBlock = (BaseBlock)o;
         return this.getType() == otherBlock.getType() && this.getData() == otherBlock.getData();
      }
   }

   public boolean equalsFuzzy(BaseBlock o) {
      return this.getType() == o.getType() && (this.getData() == o.getData() || this.getData() == -1 || o.getData() == -1);
   }

   @Deprecated
   public boolean inIterable(Iterable<BaseBlock> iter) {
      for (BaseBlock block : iter) {
         if (block.equalsFuzzy(this)) {
            return true;
         }
      }

      return false;
   }

   @Deprecated
   public static boolean containsFuzzy(Collection<BaseBlock> collection, BaseBlock o) {
      return Blocks.containsFuzzy(collection, o);
   }

   @Override
   public int hashCode() {
      int ret = this.getId() << 3;
      if (this.getData() != -1) {
         ret |= this.getData();
      }

      return ret;
   }

   @Override
   public String toString() {
      return "Block{ID:" + this.getId() + ", Data: " + this.getData() + "}";
   }
}
