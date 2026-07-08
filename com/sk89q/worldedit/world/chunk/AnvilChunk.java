package com.sk89q.worldedit.world.chunk;

import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.ByteTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.NBTUtils;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.storage.InvalidFormatException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nullable;

public class AnvilChunk implements Chunk {
   private CompoundTag rootTag;
   private byte[][] blocks;
   private byte[][] blocksAdd;
   private byte[][] data;
   private int rootX;
   private int rootZ;
   private Map<BlockVector, Map<String, Tag>> tileEntities;
   private World world;

   public AnvilChunk(World world, CompoundTag tag) throws DataException {
      this.rootTag = tag;
      this.world = world;
      this.rootX = NBTUtils.getChildTag(this.rootTag.getValue(), "xPos", IntTag.class).getValue();
      this.rootZ = NBTUtils.getChildTag(this.rootTag.getValue(), "zPos", IntTag.class).getValue();
      this.blocks = new byte[16][4096];
      this.blocksAdd = new byte[16][2048];
      this.data = new byte[16][2048];

      for (Tag rawSectionTag : NBTUtils.getChildTag(this.rootTag.getValue(), "Sections", ListTag.class).getValue()) {
         if (rawSectionTag instanceof CompoundTag) {
            CompoundTag sectionTag = (CompoundTag)rawSectionTag;
            if (sectionTag.getValue().containsKey("Y")) {
               int y = NBTUtils.getChildTag(sectionTag.getValue(), "Y", ByteTag.class).getValue();
               if (y >= 0 && y < 16) {
                  this.blocks[y] = NBTUtils.getChildTag(sectionTag.getValue(), "Blocks", ByteArrayTag.class).getValue();
                  this.data[y] = NBTUtils.getChildTag(sectionTag.getValue(), "Data", ByteArrayTag.class).getValue();
                  if (sectionTag.getValue().containsKey("Add")) {
                     this.blocksAdd[y] = NBTUtils.getChildTag(sectionTag.getValue(), "Add", ByteArrayTag.class).getValue();
                  }
               }
            }
         }
      }

      int sectionsize = 4096;

      for (byte[] block : this.blocks) {
         if (block.length != sectionsize) {
            throw new InvalidFormatException("Chunk blocks byte array expected to be " + sectionsize + " bytes; found " + block.length);
         }
      }

      for (byte[] aData : this.data) {
         if (aData.length != sectionsize / 2) {
            throw new InvalidFormatException("Chunk block data byte array expected to be " + sectionsize + " bytes; found " + aData.length);
         }
      }
   }

   @Override
   public int getBlockID(Vector position) throws DataException {
      int x = position.getBlockX() - this.rootX * 16;
      int y = position.getBlockY();
      int z = position.getBlockZ() - this.rootZ * 16;
      int section = y >> 4;
      if (section >= 0 && section < this.blocks.length) {
         int yindex = y & 15;
         if (yindex >= 0 && yindex < 16) {
            int index = x + z * 16 + yindex * 16 * 16;

            try {
               int addId = 0;
               if (index % 2 == 0) {
                  addId = (this.blocksAdd[section][index >> 1] & 15) << 8;
               } else {
                  addId = (this.blocksAdd[section][index >> 1] & 240) << 4;
               }

               return (this.blocks[section][index] & 0xFF) + addId;
            } catch (IndexOutOfBoundsException var9) {
               throw new DataException("Chunk does not contain position " + position);
            }
         } else {
            throw new DataException("Chunk does not contain position " + position);
         }
      } else {
         throw new DataException("Chunk does not contain position " + position);
      }
   }

   @Override
   public int getBlockData(Vector position) throws DataException {
      int x = position.getBlockX() - this.rootX * 16;
      int y = position.getBlockY();
      int z = position.getBlockZ() - this.rootZ * 16;
      int section = y >> 4;
      int yIndex = y & 15;
      if (section < 0 || section >= this.blocks.length) {
         throw new DataException("Chunk does not contain position " + position);
      } else if (yIndex >= 0 && yIndex < 16) {
         int index = x + z * 16 + yIndex * 16 * 16;
         boolean shift = index % 2 == 0;
         index /= 2;

         try {
            return !shift ? (this.data[section][index] & 240) >> 4 : this.data[section][index] & 15;
         } catch (IndexOutOfBoundsException var10) {
            throw new DataException("Chunk does not contain position " + position);
         }
      } else {
         throw new DataException("Chunk does not contain position " + position);
      }
   }

   private void populateTileEntities() throws DataException {
      List<Tag> tags = NBTUtils.getChildTag(this.rootTag.getValue(), "TileEntities", ListTag.class).getValue();
      this.tileEntities = new HashMap<>();

      for (Tag tag : tags) {
         if (!(tag instanceof CompoundTag)) {
            throw new InvalidFormatException("CompoundTag expected in TileEntities");
         }

         CompoundTag t = (CompoundTag)tag;
         int x = 0;
         int y = 0;
         int z = 0;
         Map<String, Tag> values = new HashMap<>();

         for (Entry<String, Tag> entry : t.getValue().entrySet()) {
            if (entry.getKey().equals("x")) {
               if (entry.getValue() instanceof IntTag) {
                  x = ((IntTag)entry.getValue()).getValue();
               }
            } else if (entry.getKey().equals("y")) {
               if (entry.getValue() instanceof IntTag) {
                  y = ((IntTag)entry.getValue()).getValue();
               }
            } else if (entry.getKey().equals("z") && entry.getValue() instanceof IntTag) {
               z = ((IntTag)entry.getValue()).getValue();
            }

            values.put(entry.getKey(), entry.getValue());
         }

         BlockVector vec = new BlockVector(x, y, z);
         this.tileEntities.put(vec, values);
      }
   }

   @Nullable
   private CompoundTag getBlockTileEntity(Vector position) throws DataException {
      if (this.tileEntities == null) {
         this.populateTileEntities();
      }

      Map<String, Tag> values = this.tileEntities.get(new BlockVector(position));
      return values == null ? null : new CompoundTag(values);
   }

   @Override
   public BaseBlock getBlock(Vector position) throws DataException {
      int id = this.getBlockID(position);
      int data = this.getBlockData(position);
      BaseBlock block = new BaseBlock(id, data);
      CompoundTag tileEntity = this.getBlockTileEntity(position);
      if (tileEntity != null) {
         block.setNbtData(tileEntity);
      }

      return block;
   }
}
