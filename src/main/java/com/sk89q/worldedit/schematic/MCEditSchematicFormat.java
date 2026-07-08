package com.sk89q.worldedit.schematic;

import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.NBTConstants;
import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.jnbt.NBTOutputStream;
import com.sk89q.jnbt.NamedTag;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.data.DataException;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MCEditSchematicFormat extends SchematicFormat {
   private static final int MAX_SIZE = 65535;

   protected MCEditSchematicFormat() {
      super("MCEdit", "mcedit", "mce");
   }

   public CuboidClipboard load(InputStream stream) throws IOException, DataException {
      NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(stream));
      Vector origin = new Vector();
      Vector offset = new Vector();
      NamedTag rootTag = nbtStream.readNamedTag();
      nbtStream.close();
      if (!rootTag.getName().equals("Schematic")) {
         throw new DataException("Tag \"Schematic\" does not exist or is not first");
      } else {
         CompoundTag schematicTag = (CompoundTag)rootTag.getTag();
         Map<String, Tag> schematic = schematicTag.getValue();
         if (!schematic.containsKey("Blocks")) {
            throw new DataException("Schematic file is missing a \"Blocks\" tag");
         } else {
            short width = getChildTag(schematic, "Width", ShortTag.class).getValue();
            short length = getChildTag(schematic, "Length", ShortTag.class).getValue();
            short height = getChildTag(schematic, "Height", ShortTag.class).getValue();

            try {
               int originX = getChildTag(schematic, "WEOriginX", IntTag.class).getValue();
               int originY = getChildTag(schematic, "WEOriginY", IntTag.class).getValue();
               int originZ = getChildTag(schematic, "WEOriginZ", IntTag.class).getValue();
               origin = new Vector(originX, originY, originZ);
            } catch (DataException var28) {
            }

            try {
               int offsetX = getChildTag(schematic, "WEOffsetX", IntTag.class).getValue();
               int offsetY = getChildTag(schematic, "WEOffsetY", IntTag.class).getValue();
               int offsetZ = getChildTag(schematic, "WEOffsetZ", IntTag.class).getValue();
               offset = new Vector(offsetX, offsetY, offsetZ);
            } catch (DataException var27) {
            }

            String materials = getChildTag(schematic, "Materials", StringTag.class).getValue();
            if (!materials.equals("Alpha")) {
               throw new DataException("Schematic file is not an Alpha schematic");
            } else {
               byte[] blockId = getChildTag(schematic, "Blocks", ByteArrayTag.class).getValue();
               byte[] blockData = getChildTag(schematic, "Data", ByteArrayTag.class).getValue();
               byte[] addId = new byte[0];
               short[] blocks = new short[blockId.length];
               if (schematic.containsKey("AddBlocks")) {
                  addId = getChildTag(schematic, "AddBlocks", ByteArrayTag.class).getValue();
               }

               for (int index = 0; index < blockId.length; index++) {
                  if (index >> 1 >= addId.length) {
                     blocks[index] = (short)(blockId[index] & 255);
                  } else if ((index & 1) == 0) {
                     blocks[index] = (short)(((addId[index >> 1] & 15) << 8) + (blockId[index] & 255));
                  } else {
                     blocks[index] = (short)(((addId[index >> 1] & 240) << 4) + (blockId[index] & 255));
                  }
               }

               List<Tag> tileEntities = getChildTag(schematic, "TileEntities", ListTag.class).getValue();
               Map<BlockVector, Map<String, Tag>> tileEntitiesMap = new HashMap<>();

               for (Tag tag : tileEntities) {
                  if (tag instanceof CompoundTag) {
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
                     tileEntitiesMap.put(vec, values);
                  }
               }

               Vector size = new Vector((int)width, (int)height, (int)length);
               CuboidClipboard clipboard = new CuboidClipboard(size);
               clipboard.setOrigin(origin);
               clipboard.setOffset(offset);

               for (int x = 0; x < width; x++) {
                  for (int y = 0; y < height; y++) {
                     for (int z = 0; z < length; z++) {
                        int indexx = y * width * length + z * width + x;
                        BlockVector pt = new BlockVector(x, y, z);
                        BaseBlock block = this.getBlockForId(blocks[indexx], blockData[indexx]);
                        if (tileEntitiesMap.containsKey(pt)) {
                           block.setNbtData(new CompoundTag(tileEntitiesMap.get(pt)));
                        }

                        clipboard.setBlock(pt, block);
                     }
                  }
               }

               return clipboard;
            }
         }
      }
   }

   @Override
   public CuboidClipboard load(File file) throws IOException, DataException {
      return this.load(new FileInputStream(file));
   }

   @Override
   public void save(CuboidClipboard clipboard, File file) throws IOException, DataException {
      int width = clipboard.getWidth();
      int height = clipboard.getHeight();
      int length = clipboard.getLength();
      if (width > 65535) {
         throw new DataException("Width of region too large for a .schematic");
      } else if (height > 65535) {
         throw new DataException("Height of region too large for a .schematic");
      } else if (length > 65535) {
         throw new DataException("Length of region too large for a .schematic");
      } else {
         HashMap<String, Tag> schematic = new HashMap<>();
         schematic.put("Width", new ShortTag((short)width));
         schematic.put("Length", new ShortTag((short)length));
         schematic.put("Height", new ShortTag((short)height));
         schematic.put("Materials", new StringTag("Alpha"));
         schematic.put("WEOriginX", new IntTag(clipboard.getOrigin().getBlockX()));
         schematic.put("WEOriginY", new IntTag(clipboard.getOrigin().getBlockY()));
         schematic.put("WEOriginZ", new IntTag(clipboard.getOrigin().getBlockZ()));
         schematic.put("WEOffsetX", new IntTag(clipboard.getOffset().getBlockX()));
         schematic.put("WEOffsetY", new IntTag(clipboard.getOffset().getBlockY()));
         schematic.put("WEOffsetZ", new IntTag(clipboard.getOffset().getBlockZ()));
         byte[] blocks = new byte[width * height * length];
         byte[] addBlocks = null;
         byte[] blockData = new byte[width * height * length];
         ArrayList<Tag> tileEntities = new ArrayList<>();

         for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
               for (int z = 0; z < length; z++) {
                  int index = y * width * length + z * width + x;
                  BaseBlock block = clipboard.getPoint(new BlockVector(x, y, z));
                  if (block.getType() > 255) {
                     if (addBlocks == null) {
                        addBlocks = new byte[(blocks.length >> 1) + 1];
                     }

                     addBlocks[index >> 1] = (byte)(
                        (index & 1) == 0
                           ? addBlocks[index >> 1] & 240 | block.getType() >> 8 & 15
                           : addBlocks[index >> 1] & 15 | (block.getType() >> 8 & 15) << 4
                     );
                  }

                  blocks[index] = (byte)block.getType();
                  blockData[index] = (byte)block.getData();
                  CompoundTag rawTag = block.getNbtData();
                  if (rawTag != null) {
                     Map<String, Tag> values = new HashMap<>();

                     for (Entry<String, Tag> entry : rawTag.getValue().entrySet()) {
                        values.put(entry.getKey(), entry.getValue());
                     }

                     values.put("id", new StringTag(block.getNbtId()));
                     values.put("x", new IntTag(x));
                     values.put("y", new IntTag(y));
                     values.put("z", new IntTag(z));
                     CompoundTag tileEntityTag = new CompoundTag(values);
                     tileEntities.add(tileEntityTag);
                  }
               }
            }
         }

         schematic.put("Blocks", new ByteArrayTag(blocks));
         schematic.put("Data", new ByteArrayTag(blockData));
         schematic.put("Entities", new ListTag(CompoundTag.class, new ArrayList<>()));
         schematic.put("TileEntities", new ListTag(CompoundTag.class, tileEntities));
         if (addBlocks != null) {
            schematic.put("AddBlocks", new ByteArrayTag(addBlocks));
         }

         CompoundTag schematicTag = new CompoundTag(schematic);
         NBTOutputStream stream = new NBTOutputStream(new GZIPOutputStream(new FileOutputStream(file)));
         stream.writeNamedTag("Schematic", schematicTag);
         stream.close();
      }
   }

   @Override
   public boolean isOfFormat(File file) {
      DataInputStream str = null;

      boolean e;
      try {
         str = new DataInputStream(new GZIPInputStream(new FileInputStream(file)));
         if ((str.readByte() & 255) == 10) {
            byte[] nameBytes = new byte[str.readShort() & '\uffff'];
            str.readFully(nameBytes);
            String name = new String(nameBytes, NBTConstants.CHARSET);
            return name.equals("Schematic");
         }

         e = false;
      } catch (IOException var16) {
         return false;
      } finally {
         if (str != null) {
            try {
               str.close();
            } catch (IOException var15) {
            }
         }
      }

      return e;
   }

   private static <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws DataException {
      if (!items.containsKey(key)) {
         throw new DataException("Schematic file is missing a \"" + key + "\" tag");
      } else {
         Tag tag = items.get(key);
         if (!expected.isInstance(tag)) {
            throw new DataException(key + " tag is not of tag type " + expected.getName());
         } else {
            return expected.cast(tag);
         }
      }
   }
}
