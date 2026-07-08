package com.sk89q.worldedit.blocks;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.NBTUtils;
import com.sk89q.jnbt.ShortTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.blocks.metadata.MobType;
import com.sk89q.worldedit.world.storage.InvalidFormatException;
import java.util.HashMap;
import java.util.Map;

public class MobSpawnerBlock extends BaseBlock implements TileEntityBlock {
   private String mobType;
   private short delay;
   private short spawnCount;
   private short spawnRange;
   private CompoundTag spawnData;
   private ListTag spawnPotentials;
   private short minSpawnDelay;
   private short maxSpawnDelay;
   private short maxNearbyEntities;
   private short requiredPlayerRange;

   public MobSpawnerBlock() {
      super(52);
      this.mobType = MobType.PIG.getName();
   }

   public MobSpawnerBlock(String mobType) {
      super(52);
      this.mobType = mobType;
   }

   public MobSpawnerBlock(int data) {
      super(52, data);
   }

   public MobSpawnerBlock(int data, String mobType) {
      super(52, data);
      this.mobType = mobType;
   }

   public String getMobType() {
      return this.mobType;
   }

   public void setMobType(String mobType) {
      this.mobType = mobType;
   }

   public short getDelay() {
      return this.delay;
   }

   public void setDelay(short delay) {
      this.delay = delay;
   }

   @Override
   public boolean hasNbtData() {
      return true;
   }

   @Override
   public String getNbtId() {
      return "MobSpawner";
   }

   @Override
   public CompoundTag getNbtData() {
      Map<String, Tag> values = new HashMap<>();
      values.put("EntityId", new StringTag(this.mobType));
      values.put("Delay", new ShortTag(this.delay));
      values.put("SpawnCount", new ShortTag(this.spawnCount));
      values.put("SpawnRange", new ShortTag(this.spawnRange));
      values.put("MinSpawnDelay", new ShortTag(this.minSpawnDelay));
      values.put("MaxSpawnDelay", new ShortTag(this.maxSpawnDelay));
      values.put("MaxNearbyEntities", new ShortTag(this.maxNearbyEntities));
      values.put("RequiredPlayerRange", new ShortTag(this.requiredPlayerRange));
      if (this.spawnData != null) {
         values.put("SpawnData", new CompoundTag(this.spawnData.getValue()));
      }

      if (this.spawnPotentials != null) {
         values.put("SpawnPotentials", new ListTag(CompoundTag.class, this.spawnPotentials.getValue()));
      }

      return new CompoundTag(values);
   }

   @Override
   public void setNbtData(CompoundTag rootTag) {
      if (rootTag != null) {
         Map<String, Tag> values = rootTag.getValue();
         Tag t = values.get("id");
         if (t instanceof StringTag && ((StringTag)t).getValue().equals("MobSpawner")) {
            StringTag mobTypeTag;
            ShortTag delayTag;
            try {
               mobTypeTag = NBTUtils.getChildTag(values, "EntityId", StringTag.class);
               delayTag = NBTUtils.getChildTag(values, "Delay", ShortTag.class);
            } catch (InvalidFormatException var23) {
               throw new RuntimeException("Invalid mob spawner data: no EntityId and/or no Delay");
            }

            this.mobType = mobTypeTag.getValue();
            this.delay = delayTag.getValue();
            ShortTag spawnCountTag = null;
            ShortTag spawnRangeTag = null;
            ShortTag minSpawnDelayTag = null;
            ShortTag maxSpawnDelayTag = null;
            ShortTag maxNearbyEntitiesTag = null;
            ShortTag requiredPlayerRangeTag = null;
            ListTag spawnPotentialsTag = null;
            CompoundTag spawnDataTag = null;

            try {
               spawnCountTag = NBTUtils.getChildTag(values, "SpawnCount", ShortTag.class);
            } catch (InvalidFormatException var22) {
            }

            try {
               spawnRangeTag = NBTUtils.getChildTag(values, "SpawnRange", ShortTag.class);
            } catch (InvalidFormatException var21) {
            }

            try {
               minSpawnDelayTag = NBTUtils.getChildTag(values, "MinSpawnDelay", ShortTag.class);
            } catch (InvalidFormatException var20) {
            }

            try {
               maxSpawnDelayTag = NBTUtils.getChildTag(values, "MaxSpawnDelay", ShortTag.class);
            } catch (InvalidFormatException var19) {
            }

            try {
               maxNearbyEntitiesTag = NBTUtils.getChildTag(values, "MaxNearbyEntities", ShortTag.class);
            } catch (InvalidFormatException var18) {
            }

            try {
               requiredPlayerRangeTag = NBTUtils.getChildTag(values, "RequiredPlayerRange", ShortTag.class);
            } catch (InvalidFormatException var17) {
            }

            try {
               spawnPotentialsTag = NBTUtils.getChildTag(values, "SpawnPotentials", ListTag.class);
            } catch (InvalidFormatException var16) {
            }

            try {
               spawnDataTag = NBTUtils.getChildTag(values, "SpawnData", CompoundTag.class);
            } catch (InvalidFormatException var15) {
            }

            if (spawnCountTag != null) {
               this.spawnCount = spawnCountTag.getValue();
            }

            if (spawnRangeTag != null) {
               this.spawnRange = spawnRangeTag.getValue();
            }

            if (minSpawnDelayTag != null) {
               this.minSpawnDelay = minSpawnDelayTag.getValue();
            }

            if (maxSpawnDelayTag != null) {
               this.maxSpawnDelay = maxSpawnDelayTag.getValue();
            }

            if (maxNearbyEntitiesTag != null) {
               this.maxNearbyEntities = maxNearbyEntitiesTag.getValue();
            }

            if (requiredPlayerRangeTag != null) {
               this.requiredPlayerRange = requiredPlayerRangeTag.getValue();
            }

            if (spawnPotentialsTag != null) {
               this.spawnPotentials = new ListTag(CompoundTag.class, spawnPotentialsTag.getValue());
            }

            if (spawnDataTag != null) {
               this.spawnData = new CompoundTag(spawnDataTag.getValue());
            }
         } else {
            throw new RuntimeException("'MobSpawner' tile entity expected");
         }
      }
   }
}
