package com.sk89q.worldedit.blocks;

import com.sk89q.jnbt.ByteTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import java.util.HashMap;
import java.util.Map;

public class SkullBlock extends BaseBlock implements TileEntityBlock {
   private String owner = "";
   private byte skullType;
   private byte rot;

   public SkullBlock(int data) {
      this(data, (byte)0);
   }

   public SkullBlock(int data, byte type) {
      this(data, type, (byte)0);
   }

   public SkullBlock(int data, byte type, byte rot) {
      super(144, data);
      if (type >= 0 && type <= 4) {
         this.skullType = type;
      } else {
         this.skullType = 0;
      }

      this.rot = rot;
      this.owner = "";
   }

   public SkullBlock(int data, byte rot, String owner) {
      super(144, data);
      this.rot = rot;
      this.setOwner(owner);
      if (owner == null || owner.isEmpty()) {
         this.skullType = 0;
      }
   }

   public void setOwner(String owner) {
      if (owner == null) {
         this.owner = "";
      } else if (owner.length() <= 16 && !owner.isEmpty()) {
         this.owner = owner;
      } else {
         this.owner = "";
      }

      if (this.owner != null && !this.owner.isEmpty()) {
         this.skullType = 3;
      }
   }

   public String getOwner() {
      return this.owner;
   }

   public byte getSkullType() {
      return this.skullType;
   }

   public void setSkullType(byte skullType) {
      this.skullType = skullType;
   }

   public byte getRot() {
      return this.rot;
   }

   public void setRot(byte rot) {
      this.rot = rot;
   }

   @Override
   public boolean hasNbtData() {
      return true;
   }

   @Override
   public String getNbtId() {
      return "Skull";
   }

   @Override
   public CompoundTag getNbtData() {
      Map<String, Tag> values = new HashMap<>();
      values.put("SkullType", new ByteTag(this.skullType));
      if (this.owner == null) {
         this.owner = "";
      }

      values.put("ExtraType", new StringTag(this.owner));
      values.put("Rot", new ByteTag(this.rot));
      return new CompoundTag(values);
   }

   @Override
   public void setNbtData(CompoundTag rootTag) {
      if (rootTag != null) {
         Map<String, Tag> values = rootTag.getValue();
         Tag t = values.get("id");
         if (t instanceof StringTag && ((StringTag)t).getValue().equals("Skull")) {
            t = values.get("SkullType");
            if (t instanceof ByteTag) {
               this.skullType = ((ByteTag)t).getValue();
            }

            t = values.get("ExtraType");
            if (t != null && t instanceof StringTag) {
               this.owner = ((StringTag)t).getValue();
            }

            t = values.get("Rot");
            if (t instanceof ByteTag) {
               this.rot = ((ByteTag)t).getValue();
            }
         } else {
            throw new RuntimeException("'Skull' tile entity expected");
         }
      }
   }
}
