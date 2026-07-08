package com.sk89q.worldedit.bukkit.adapter.impl;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.ByteArrayTag;
import com.sk89q.jnbt.ByteTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.DoubleTag;
import com.sk89q.jnbt.EndTag;
import com.sk89q.jnbt.FloatTag;
import com.sk89q.jnbt.IntArrayTag;
import com.sk89q.jnbt.IntTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.LongTag;
import com.sk89q.jnbt.NBTConstants;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.internal.Constants;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import net.minecraft.server.v1_8_R2.BiomeBase;
import net.minecraft.server.v1_8_R2.BlockPosition;
import net.minecraft.server.v1_8_R2.Entity;
import net.minecraft.server.v1_8_R2.EntityTypes;
import net.minecraft.server.v1_8_R2.NBTBase;
import net.minecraft.server.v1_8_R2.NBTTagByte;
import net.minecraft.server.v1_8_R2.NBTTagByteArray;
import net.minecraft.server.v1_8_R2.NBTTagCompound;
import net.minecraft.server.v1_8_R2.NBTTagDouble;
import net.minecraft.server.v1_8_R2.NBTTagEnd;
import net.minecraft.server.v1_8_R2.NBTTagFloat;
import net.minecraft.server.v1_8_R2.NBTTagInt;
import net.minecraft.server.v1_8_R2.NBTTagIntArray;
import net.minecraft.server.v1_8_R2.NBTTagList;
import net.minecraft.server.v1_8_R2.NBTTagLong;
import net.minecraft.server.v1_8_R2.NBTTagShort;
import net.minecraft.server.v1_8_R2.NBTTagString;
import net.minecraft.server.v1_8_R2.TileEntity;
import net.minecraft.server.v1_8_R2.World;
import net.minecraft.server.v1_8_R2.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R2.CraftServer;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftEntity;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public final class Spigot_v1_8_R2 implements BukkitImplAdapter {
   private final Logger logger = Logger.getLogger(this.getClass().getCanonicalName());
   private final Field nbtListTagListField;
   private final Method nbtCreateTagMethod;

   public Spigot_v1_8_R2() throws NoSuchFieldException, NoSuchMethodException {
      CraftServer.class.cast(Bukkit.getServer());
      this.nbtListTagListField = NBTTagList.class.getDeclaredField("list");
      this.nbtListTagListField.setAccessible(true);
      this.nbtCreateTagMethod = NBTBase.class.getDeclaredMethod("createTag", byte.class);
      this.nbtCreateTagMethod.setAccessible(true);
   }

   private static void readTagIntoTileEntity(NBTTagCompound tag, TileEntity tileEntity) {
      tileEntity.a(tag);
   }

   private static void readTileEntityIntoTag(TileEntity tileEntity, NBTTagCompound tag) {
      tileEntity.b(tag);
   }

   @Nullable
   private static String getEntityId(Entity entity) {
      return EntityTypes.b(entity);
   }

   @Nullable
   private static Entity createEntityFromId(String id, World world) {
      return EntityTypes.createEntityByName(id, world);
   }

   private static void readTagIntoEntity(NBTTagCompound tag, Entity entity) {
      entity.f(tag);
   }

   private static void readEntityIntoTag(Entity entity, NBTTagCompound tag) {
      entity.e(tag);
   }

   @Override
   public int getBlockId(Material material) {
      return material.getId();
   }

   @Override
   public Material getMaterial(int id) {
      return Material.getMaterial(id);
   }

   @Override
   public int getBiomeId(Biome biome) {
      BiomeBase mcBiome = CraftBlock.biomeToBiomeBase(biome);
      return mcBiome != null ? mcBiome.id : 0;
   }

   @Override
   public Biome getBiome(int id) {
      BiomeBase mcBiome = BiomeBase.getBiome(id);
      return CraftBlock.biomeBaseToBiome(mcBiome);
   }

   @Override
   public BaseBlock getBlock(Location location) {
      Preconditions.checkNotNull(location);
      CraftWorld craftWorld = (CraftWorld)location.getWorld();
      int x = location.getBlockX();
      int y = location.getBlockY();
      int z = location.getBlockZ();
      Block bukkitBlock = location.getBlock();
      BaseBlock block = new BaseBlock(bukkitBlock.getTypeId(), bukkitBlock.getData());
      TileEntity te = craftWorld.getHandle().getTileEntity(new BlockPosition(x, y, z));
      if (te != null) {
         NBTTagCompound tag = new NBTTagCompound();
         readTileEntityIntoTag(te, tag);
         block.setNbtData((CompoundTag)this.toNative(tag));
      }

      return block;
   }

   @Override
   public boolean setBlock(Location location, BaseBlock block, boolean notifyAndLight) {
      Preconditions.checkNotNull(location);
      Preconditions.checkNotNull(block);
      CraftWorld craftWorld = (CraftWorld)location.getWorld();
      int x = location.getBlockX();
      int y = location.getBlockY();
      int z = location.getBlockZ();
      boolean changed = location.getBlock().setTypeIdAndData(block.getId(), (byte)block.getData(), notifyAndLight);
      CompoundTag nativeTag = block.getNbtData();
      if (nativeTag != null) {
         TileEntity tileEntity = craftWorld.getHandle().getTileEntity(new BlockPosition(x, y, z));
         if (tileEntity != null) {
            NBTTagCompound tag = (NBTTagCompound)this.fromNative(nativeTag);
            tag.set("x", new NBTTagInt(x));
            tag.set("y", new NBTTagInt(y));
            tag.set("z", new NBTTagInt(z));
            readTagIntoTileEntity(tag, tileEntity);
         }
      }

      return changed;
   }

   @Override
   public BaseEntity getEntity(org.bukkit.entity.Entity entity) {
      Preconditions.checkNotNull(entity);
      CraftEntity craftEntity = (CraftEntity)entity;
      Entity mcEntity = craftEntity.getHandle();
      String id = getEntityId(mcEntity);
      if (id != null) {
         NBTTagCompound tag = new NBTTagCompound();
         readEntityIntoTag(mcEntity, tag);
         return new BaseEntity(id, (CompoundTag)this.toNative(tag));
      } else {
         return null;
      }
   }

   @Nullable
   @Override
   public org.bukkit.entity.Entity createEntity(Location location, BaseEntity state) {
      Preconditions.checkNotNull(location);
      Preconditions.checkNotNull(state);
      CraftWorld craftWorld = (CraftWorld)location.getWorld();
      WorldServer worldServer = craftWorld.getHandle();
      Entity createdEntity = createEntityFromId(state.getTypeId(), craftWorld.getHandle());
      if (createdEntity == null) {
         return null;
      } else {
         CompoundTag nativeTag = state.getNbtData();
         if (nativeTag != null) {
            NBTTagCompound tag = (NBTTagCompound)this.fromNative(nativeTag);

            for (String name : Constants.NO_COPY_ENTITY_NBT_FIELDS) {
               tag.remove(name);
            }

            readTagIntoEntity(tag, createdEntity);
         }

         createdEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
         worldServer.addEntity(createdEntity, SpawnReason.CUSTOM);
         return createdEntity.getBukkitEntity();
      }
   }

   private Tag toNative(NBTBase foreign) {
      if (foreign == null) {
         return null;
      } else if (!(foreign instanceof NBTTagCompound)) {
         if (foreign instanceof NBTTagByte) {
            return new ByteTag(((NBTTagByte)foreign).f());
         } else if (foreign instanceof NBTTagByteArray) {
            return new ByteArrayTag(((NBTTagByteArray)foreign).c());
         } else if (foreign instanceof NBTTagDouble) {
            return new DoubleTag(((NBTTagDouble)foreign).g());
         } else if (foreign instanceof NBTTagFloat) {
            return new FloatTag(((NBTTagFloat)foreign).h());
         } else if (foreign instanceof NBTTagInt) {
            return new IntTag(((NBTTagInt)foreign).d());
         } else if (foreign instanceof NBTTagIntArray) {
            return new IntArrayTag(((NBTTagIntArray)foreign).c());
         } else if (foreign instanceof NBTTagList) {
            try {
               return this.toNativeList((NBTTagList)foreign);
            } catch (Throwable var7) {
               this.logger.log(Level.WARNING, "Failed to convert NBTTagList", var7);
               return new ListTag(ByteTag.class, new ArrayList<>());
            }
         } else if (foreign instanceof NBTTagLong) {
            return new LongTag(((NBTTagLong)foreign).c());
         } else if (foreign instanceof NBTTagShort) {
            return new ShortTag(((NBTTagShort)foreign).e());
         } else if (foreign instanceof NBTTagString) {
            return new StringTag(((NBTTagString)foreign).a_());
         } else if (foreign instanceof NBTTagEnd) {
            return new EndTag();
         } else {
            throw new IllegalArgumentException("Don't know how to make native " + foreign.getClass().getCanonicalName());
         }
      } else {
         Map<String, Tag> values = new HashMap<>();

         for (String str : ((NBTTagCompound)foreign).c()) {
            NBTBase base = ((NBTTagCompound)foreign).get(str);
            values.put(str, this.toNative(base));
         }

         return new CompoundTag(values);
      }
   }

   private ListTag toNativeList(NBTTagList foreign) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
      List<Tag> values = new ArrayList<>();
      int type = foreign.f();
      List foreignList = (List)this.nbtListTagListField.get(foreign);

      for (int i = 0; i < foreign.size(); i++) {
         NBTBase element = (NBTBase)foreignList.get(i);
         values.add(this.toNative(element));
      }

      Class<? extends Tag> cls = NBTConstants.getClassFromType(type);
      return new ListTag(cls, values);
   }

   private NBTBase fromNative(Tag foreign) {
      if (foreign == null) {
         return null;
      } else if (foreign instanceof CompoundTag) {
         NBTTagCompound tag = new NBTTagCompound();

         for (Entry<String, Tag> entry : ((CompoundTag)foreign).getValue().entrySet()) {
            tag.set(entry.getKey(), this.fromNative(entry.getValue()));
         }

         return tag;
      } else if (foreign instanceof ByteTag) {
         return new NBTTagByte(((ByteTag)foreign).getValue());
      } else if (foreign instanceof ByteArrayTag) {
         return new NBTTagByteArray(((ByteArrayTag)foreign).getValue());
      } else if (foreign instanceof DoubleTag) {
         return new NBTTagDouble(((DoubleTag)foreign).getValue());
      } else if (foreign instanceof FloatTag) {
         return new NBTTagFloat(((FloatTag)foreign).getValue());
      } else if (foreign instanceof IntTag) {
         return new NBTTagInt(((IntTag)foreign).getValue());
      } else if (foreign instanceof IntArrayTag) {
         return new NBTTagIntArray(((IntArrayTag)foreign).getValue());
      } else if (!(foreign instanceof ListTag)) {
         if (foreign instanceof LongTag) {
            return new NBTTagLong(((LongTag)foreign).getValue());
         } else if (foreign instanceof ShortTag) {
            return new NBTTagShort(((ShortTag)foreign).getValue());
         } else if (foreign instanceof StringTag) {
            return new NBTTagString(((StringTag)foreign).getValue());
         } else if (foreign instanceof EndTag) {
            try {
               return (NBTBase)this.nbtCreateTagMethod.invoke(null, (byte)0);
            } catch (Exception var6) {
               return null;
            }
         } else {
            throw new IllegalArgumentException("Don't know how to make NMS " + foreign.getClass().getCanonicalName());
         }
      } else {
         NBTTagList tag = new NBTTagList();
         ListTag foreignList = (ListTag)foreign;

         for (Tag t : foreignList.getValue()) {
            tag.add(this.fromNative(t));
         }

         return tag;
      }
   }
}
