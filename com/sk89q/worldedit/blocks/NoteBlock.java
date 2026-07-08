package com.sk89q.worldedit.blocks;

import com.sk89q.jnbt.ByteTag;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import java.util.HashMap;
import java.util.Map;

public class NoteBlock extends BaseBlock implements TileEntityBlock {
   private byte note;

   public NoteBlock() {
      super(25);
      this.note = 0;
   }

   public NoteBlock(int data) {
      super(25, data);
      this.note = 0;
   }

   public NoteBlock(int data, byte note) {
      super(25, data);
      this.note = note;
   }

   public byte getNote() {
      return this.note;
   }

   public void setNote(byte note) {
      this.note = note;
   }

   @Override
   public boolean hasNbtData() {
      return true;
   }

   @Override
   public String getNbtId() {
      return "Music";
   }

   @Override
   public CompoundTag getNbtData() {
      Map<String, Tag> values = new HashMap<>();
      values.put("note", new ByteTag(this.note));
      return new CompoundTag(values);
   }

   @Override
   public void setNbtData(CompoundTag rootTag) {
      if (rootTag != null) {
         Map<String, Tag> values = rootTag.getValue();
         Tag t = values.get("id");
         if (t instanceof StringTag && ((StringTag)t).getValue().equals("Music")) {
            t = values.get("note");
            if (t instanceof ByteTag) {
               this.note = ((ByteTag)t).getValue();
            }
         } else {
            throw new RuntimeException("'Music' tile entity expected");
         }
      }
   }
}
