package com.sk89q.jnbt;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class CompoundTag extends Tag {
   private final Map<String, Tag> value;

   public CompoundTag(Map<String, Tag> value) {
      this.value = Collections.unmodifiableMap(value);
   }

   public boolean containsKey(String key) {
      return this.value.containsKey(key);
   }

   public Map<String, Tag> getValue() {
      return this.value;
   }

   public CompoundTag setValue(Map<String, Tag> value) {
      return new CompoundTag(value);
   }

   public CompoundTagBuilder createBuilder() {
      return new CompoundTagBuilder(new HashMap<>(this.value));
   }

   public byte[] getByteArray(String key) {
      Tag tag = this.value.get(key);
      return tag instanceof ByteArrayTag ? ((ByteArrayTag)tag).getValue() : new byte[0];
   }

   public byte getByte(String key) {
      Tag tag = this.value.get(key);
      return tag instanceof ByteTag ? ((ByteTag)tag).getValue() : 0;
   }

   public double getDouble(String key) {
      Tag tag = this.value.get(key);
      return tag instanceof DoubleTag ? ((DoubleTag)tag).getValue() : 0.0;
   }

   public double asDouble(String key) {
      Tag tag = this.value.get(key);
      if (tag instanceof ByteTag) {
         return ((ByteTag)tag).getValue().byteValue();
      } else if (tag instanceof ShortTag) {
         return ((ShortTag)tag).getValue().shortValue();
      } else if (tag instanceof IntTag) {
         return ((IntTag)tag).getValue().intValue();
      } else if (tag instanceof LongTag) {
         return ((LongTag)tag).getValue().longValue();
      } else if (tag instanceof FloatTag) {
         return ((FloatTag)tag).getValue().floatValue();
      } else {
         return tag instanceof DoubleTag ? ((DoubleTag)tag).getValue() : 0.0;
      }
   }

   public float getFloat(String key) {
      Tag tag = this.value.get(key);
      return tag instanceof FloatTag ? ((FloatTag)tag).getValue() : 0.0F;
   }

   public int[] getIntArray(String key) {
      Tag tag = this.value.get(key);
      return tag instanceof IntArrayTag ? ((IntArrayTag)tag).getValue() : new int[0];
   }

   public int getInt(String key) {
      Tag tag = this.value.get(key);
      return tag instanceof IntTag ? ((IntTag)tag).getValue() : 0;
   }

   public int asInt(String key) {
      Tag tag = this.value.get(key);
      if (tag instanceof ByteTag) {
         return ((ByteTag)tag).getValue();
      } else if (tag instanceof ShortTag) {
         return ((ShortTag)tag).getValue();
      } else if (tag instanceof IntTag) {
         return ((IntTag)tag).getValue();
      } else if (tag instanceof LongTag) {
         return ((LongTag)tag).getValue().intValue();
      } else if (tag instanceof FloatTag) {
         return ((FloatTag)tag).getValue().intValue();
      } else {
         return tag instanceof DoubleTag ? ((DoubleTag)tag).getValue().intValue() : 0;
      }
   }

   public List<Tag> getList(String key) {
      Tag tag = this.value.get(key);
      return tag instanceof ListTag ? ((ListTag)tag).getValue() : Collections.emptyList();
   }

   public ListTag getListTag(String key) {
      Tag tag = this.value.get(key);
      return tag instanceof ListTag ? (ListTag)tag : new ListTag(StringTag.class, Collections.emptyList());
   }

   public <T extends Tag> List<T> getList(String key, Class<T> listType) {
      Tag tag = this.value.get(key);
      if (tag instanceof ListTag) {
         ListTag listTag = (ListTag)tag;
         return (List<T>)(listTag.getType().equals(listType) ? listTag.getValue() : Collections.emptyList());
      } else {
         return Collections.emptyList();
      }
   }

   public long getLong(String key) {
      Tag tag = this.value.get(key);
      return tag instanceof LongTag ? ((LongTag)tag).getValue() : 0L;
   }

   public long asLong(String key) {
      Tag tag = this.value.get(key);
      if (tag instanceof ByteTag) {
         return ((ByteTag)tag).getValue().byteValue();
      } else if (tag instanceof ShortTag) {
         return ((ShortTag)tag).getValue().shortValue();
      } else if (tag instanceof IntTag) {
         return ((IntTag)tag).getValue().intValue();
      } else if (tag instanceof LongTag) {
         return ((LongTag)tag).getValue();
      } else if (tag instanceof FloatTag) {
         return ((FloatTag)tag).getValue().longValue();
      } else {
         return tag instanceof DoubleTag ? ((DoubleTag)tag).getValue().longValue() : 0L;
      }
   }

   public short getShort(String key) {
      Tag tag = this.value.get(key);
      return tag instanceof ShortTag ? ((ShortTag)tag).getValue() : 0;
   }

   public String getString(String key) {
      Tag tag = this.value.get(key);
      return tag instanceof StringTag ? ((StringTag)tag).getValue() : "";
   }

   @Override
   public String toString() {
      StringBuilder bldr = new StringBuilder();
      bldr.append("TAG_Compound").append(": ").append(this.value.size()).append(" entries\r\n{\r\n");

      for (Entry<String, Tag> entry : this.value.entrySet()) {
         bldr.append("   ").append(entry.getValue().toString().replaceAll("\r\n", "\r\n   ")).append("\r\n");
      }

      bldr.append("}");
      return bldr.toString();
   }
}
