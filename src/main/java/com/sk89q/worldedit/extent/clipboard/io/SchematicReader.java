package com.sk89q.worldedit.extent.clipboard.io;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.jnbt.NamedTag;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.registry.WorldData;
import com.sk89q.worldedit.world.storage.NBTConversions;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

public class SchematicReader implements ClipboardReader {
   private static final Logger log = Logger.getLogger(SchematicReader.class.getCanonicalName());
   private final NBTInputStream inputStream;

   public SchematicReader(NBTInputStream inputStream) {
      Preconditions.checkNotNull(inputStream);
      this.inputStream = inputStream;
   }

   @Override
   public Clipboard read(WorldData data) throws IOException {
      NamedTag rootTag = this.inputStream.readNamedTag();
      if (!rootTag.getName().equals("Schematic")) {
         throw new IOException("Tag 'Schematic' does not exist or is not first");
      } else {
         CompoundTag schematicTag = (CompoundTag)rootTag.getTag();
         Map<String, Tag> schematic = schematicTag.getValue();
         if (!schematic.containsKey("Blocks")) {
            throw new IOException("Schematic file is missing a 'Blocks' tag");
         } else {
            String materials = requireTag(schematic, "Materials", StringTag.class).getValue();
            if (!materials.equals("Alpha")) {
               throw new IOException("Schematic file is not an Alpha schematic");
            } else {
               short width = requireTag(schematic, "Width", ShortTag.class).getValue();
               short height = requireTag(schematic, "Height", ShortTag.class).getValue();
               short length = requireTag(schematic, "Length", ShortTag.class).getValue();

               Vector origin;
               Region region;
               try {
                  int originX = requireTag(schematic, "WEOriginX", IntTag.class).getValue();
                  int originY = requireTag(schematic, "WEOriginY", IntTag.class).getValue();
                  int originZ = requireTag(schematic, "WEOriginZ", IntTag.class).getValue();
                  Vector min = new Vector(originX, originY, originZ);
                  int offsetX = requireTag(schematic, "WEOffsetX", IntTag.class).getValue();
                  int offsetY = requireTag(schematic, "WEOffsetY", IntTag.class).getValue();
                  int offsetZ = requireTag(schematic, "WEOffsetZ", IntTag.class).getValue();
                  Vector offset = new Vector(offsetX, offsetY, offsetZ);
                  origin = min.subtract(offset);
                  region = new CuboidRegion(min, min.add((int)width, (int)height, (int)length).subtract(Vector.ONE));
               } catch (IOException var26) {
                  origin = new Vector(0, 0, 0);
                  region = new CuboidRegion(origin, origin.add((int)width, (int)height, (int)length).subtract(Vector.ONE));
               }

               byte[] blockId = requireTag(schematic, "Blocks", ByteArrayTag.class).getValue();
               byte[] blockData = requireTag(schematic, "Data", ByteArrayTag.class).getValue();
               byte[] addId = new byte[0];
               short[] blocks = new short[blockId.length];
               if (schematic.containsKey("AddBlocks")) {
                  addId = requireTag(schematic, "AddBlocks", ByteArrayTag.class).getValue();
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

               List<Tag> tileEntities = requireTag(schematic, "TileEntities", ListTag.class).getValue();
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

               BlockArrayClipboard clipboard = new BlockArrayClipboard(region);
               clipboard.setOrigin(origin);
               int failedBlockSets = 0;

               for (int x = 0; x < width; x++) {
                  for (int y = 0; y < height; y++) {
                     for (int z = 0; z < length; z++) {
                        int indexx = y * width * length + z * width + x;
                        BlockVector pt = new BlockVector(x, y, z);
                        BaseBlock block = new BaseBlock(blocks[indexx], blockData[indexx]);
                        if (tileEntitiesMap.containsKey(pt)) {
                           block.setNbtData(new CompoundTag(tileEntitiesMap.get(pt)));
                        }

                        try {
                           clipboard.setBlock(region.getMinimumPoint().add(pt), block);
                        } catch (WorldEditException var28) {
                           switch (failedBlockSets) {
                              case 0:
                                 log.log(Level.WARNING, "Failed to set block on a Clipboard", (Throwable)var28);
                                 break;
                              case 1:
                                 log.log(Level.WARNING, "Failed to set block on a Clipboard (again) -- no more messages will be logged", (Throwable)var28);
                           }

                           failedBlockSets++;
                        }
                     }
                  }
               }

               try {
                  for (Tag tagx : requireTag(schematic, "Entities", ListTag.class).getValue()) {
                     if (tagx instanceof CompoundTag) {
                        CompoundTag compound = (CompoundTag)tagx;
                        String id = compound.getString("id");
                        Location location = NBTConversions.toLocation(clipboard, compound.getListTag("Pos"), compound.getListTag("Rotation"));
                        if (!id.isEmpty()) {
                           BaseEntity state = new BaseEntity(id, compound);
                           clipboard.createEntity(location, state);
                        }
                     }
                  }
               } catch (IOException var27) {
               }

               return clipboard;
            }
         }
      }
   }

   private static <T extends Tag> T requireTag(Map<String, Tag> items, String key, Class<T> expected) throws IOException {
      if (!items.containsKey(key)) {
         throw new IOException("Schematic file is missing a \"" + key + "\" tag");
      } else {
         Tag tag = items.get(key);
         if (!expected.isInstance(tag)) {
            throw new IOException(key + " tag is not of tag type " + expected.getName());
         } else {
            return expected.cast(tag);
         }
      }
   }

   @Nullable
   private static <T extends Tag> T getTag(CompoundTag tag, Class<T> expected, String key) {
      Map<String, Tag> items = tag.getValue();
      if (!items.containsKey(key)) {
         return null;
      } else {
         Tag test = items.get(key);
         return !expected.isInstance(test) ? null : expected.cast(test);
      }
   }
}
