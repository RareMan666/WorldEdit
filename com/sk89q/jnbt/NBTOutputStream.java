package com.sk89q.jnbt;

import com.google.common.base.Preconditions;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map.Entry;

public final class NBTOutputStream implements Closeable {
   private final DataOutputStream os;

   public NBTOutputStream(OutputStream os) throws IOException {
      this.os = new DataOutputStream(os);
   }

   public void writeNamedTag(String name, Tag tag) throws IOException {
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(tag);
      int type = NBTUtils.getTypeCode((Class<? extends Tag>)tag.getClass());
      byte[] nameBytes = name.getBytes(NBTConstants.CHARSET);
      this.os.writeByte(type);
      this.os.writeShort(nameBytes.length);
      this.os.write(nameBytes);
      if (type == 0) {
         throw new IOException("Named TAG_End not permitted.");
      } else {
         this.writeTagPayload(tag);
      }
   }

   private void writeTagPayload(Tag tag) throws IOException {
      int type = NBTUtils.getTypeCode((Class<? extends Tag>)tag.getClass());
      switch (type) {
         case 0:
            this.writeEndTagPayload((EndTag)tag);
            break;
         case 1:
            this.writeByteTagPayload((ByteTag)tag);
            break;
         case 2:
            this.writeShortTagPayload((ShortTag)tag);
            break;
         case 3:
            this.writeIntTagPayload((IntTag)tag);
            break;
         case 4:
            this.writeLongTagPayload((LongTag)tag);
            break;
         case 5:
            this.writeFloatTagPayload((FloatTag)tag);
            break;
         case 6:
            this.writeDoubleTagPayload((DoubleTag)tag);
            break;
         case 7:
            this.writeByteArrayTagPayload((ByteArrayTag)tag);
            break;
         case 8:
            this.writeStringTagPayload((StringTag)tag);
            break;
         case 9:
            this.writeListTagPayload((ListTag)tag);
            break;
         case 10:
            this.writeCompoundTagPayload((CompoundTag)tag);
            break;
         case 11:
            this.writeIntArrayTagPayload((IntArrayTag)tag);
            break;
         default:
            throw new IOException("Invalid tag type: " + type + ".");
      }
   }

   private void writeByteTagPayload(ByteTag tag) throws IOException {
      this.os.writeByte(tag.getValue());
   }

   private void writeByteArrayTagPayload(ByteArrayTag tag) throws IOException {
      byte[] bytes = tag.getValue();
      this.os.writeInt(bytes.length);
      this.os.write(bytes);
   }

   private void writeCompoundTagPayload(CompoundTag tag) throws IOException {
      for (Entry<String, Tag> entry : tag.getValue().entrySet()) {
         this.writeNamedTag(entry.getKey(), entry.getValue());
      }

      this.os.writeByte(0);
   }

   private void writeListTagPayload(ListTag tag) throws IOException {
      Class<? extends Tag> clazz = tag.getType();
      List<Tag> tags = tag.getValue();
      int size = tags.size();
      this.os.writeByte(NBTUtils.getTypeCode(clazz));
      this.os.writeInt(size);

      for (Tag tag1 : tags) {
         this.writeTagPayload(tag1);
      }
   }

   private void writeStringTagPayload(StringTag tag) throws IOException {
      byte[] bytes = tag.getValue().getBytes(NBTConstants.CHARSET);
      this.os.writeShort(bytes.length);
      this.os.write(bytes);
   }

   private void writeDoubleTagPayload(DoubleTag tag) throws IOException {
      this.os.writeDouble(tag.getValue());
   }

   private void writeFloatTagPayload(FloatTag tag) throws IOException {
      this.os.writeFloat(tag.getValue());
   }

   private void writeLongTagPayload(LongTag tag) throws IOException {
      this.os.writeLong(tag.getValue());
   }

   private void writeIntTagPayload(IntTag tag) throws IOException {
      this.os.writeInt(tag.getValue());
   }

   private void writeShortTagPayload(ShortTag tag) throws IOException {
      this.os.writeShort(tag.getValue());
   }

   private void writeEndTagPayload(EndTag tag) {
   }

   private void writeIntArrayTagPayload(IntArrayTag tag) throws IOException {
      int[] data = tag.getValue();
      this.os.writeInt(data.length);

      for (int aData : data) {
         this.os.writeInt(aData);
      }
   }

   @Override
   public void close() throws IOException {
      this.os.close();
   }
}
