package com.sk89q.worldedit.blocks;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.util.gson.GsonUtil;
import java.util.HashMap;
import java.util.Map;

public class SignBlock extends BaseBlock implements TileEntityBlock {
   private String[] text;
   private static String EMPTY = "{\"text\":\"\"}";

   public SignBlock(int type, int data) {
      super(type, data);
      this.text = new String[]{EMPTY, EMPTY, EMPTY, EMPTY};
   }

   public SignBlock(int type, int data, String[] text) {
      super(type, data);
      if (text == null) {
         this.text = new String[]{EMPTY, EMPTY, EMPTY, EMPTY};
      } else {
         for (int i = 0; i < text.length; i++) {
            if (text[i].isEmpty()) {
               text[i] = EMPTY;
            } else {
               text[i] = "{\"text\":" + GsonUtil.stringValue(text[i]) + "}";
            }
         }

         this.text = text;
      }
   }

   public String[] getText() {
      return this.text;
   }

   public void setText(String[] text) {
      if (text == null) {
         throw new IllegalArgumentException("Can't set null text for a sign");
      } else {
         this.text = text;
      }
   }

   @Override
   public boolean hasNbtData() {
      return true;
   }

   @Override
   public String getNbtId() {
      return "Sign";
   }

   @Override
   public CompoundTag getNbtData() {
      Map<String, Tag> values = new HashMap<>();
      values.put("Text1", new StringTag(this.text[0]));
      values.put("Text2", new StringTag(this.text[1]));
      values.put("Text3", new StringTag(this.text[2]));
      values.put("Text4", new StringTag(this.text[3]));
      return new CompoundTag(values);
   }

   @Override
   public void setNbtData(CompoundTag rootTag) {
      if (rootTag != null) {
         Map<String, Tag> values = rootTag.getValue();
         this.text = new String[]{EMPTY, EMPTY, EMPTY, EMPTY};
         Tag t = values.get("id");
         if (t instanceof StringTag && ((StringTag)t).getValue().equals("Sign")) {
            t = values.get("Text1");
            if (t instanceof StringTag) {
               this.text[0] = ((StringTag)t).getValue();
            }

            t = values.get("Text2");
            if (t instanceof StringTag) {
               this.text[1] = ((StringTag)t).getValue();
            }

            t = values.get("Text3");
            if (t instanceof StringTag) {
               this.text[2] = ((StringTag)t).getValue();
            }

            t = values.get("Text4");
            if (t instanceof StringTag) {
               this.text[3] = ((StringTag)t).getValue();
            }
         } else {
            throw new RuntimeException("'Sign' tile entity expected");
         }
      }
   }
}
