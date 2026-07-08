package com.sk89q.worldedit.blocks;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.ListTag;
import com.sk89q.jnbt.NBTUtils;
import com.sk89q.jnbt.StringTag;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.storage.InvalidFormatException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChestBlock extends ContainerBlock {
   public ChestBlock() {
      super(54, 27);
   }

   public ChestBlock(int data) {
      super(54, data, 27);
   }

   public ChestBlock(int data, BaseItemStack[] items) {
      super(54, data, 27);
      this.setItems(items);
   }

   @Override
   public String getNbtId() {
      return "Chest";
   }

   @Override
   public CompoundTag getNbtData() {
      Map<String, Tag> values = new HashMap<>();
      values.put("Items", new ListTag(CompoundTag.class, this.serializeInventory(this.getItems())));
      return new CompoundTag(values);
   }

   @Override
   public void setNbtData(CompoundTag rootTag) {
      if (rootTag != null) {
         Map<String, Tag> values = rootTag.getValue();
         Tag t = values.get("id");
         if (t instanceof StringTag && ((StringTag)t).getValue().equals("Chest")) {
            List<CompoundTag> items = new ArrayList<>();

            try {
               for (Tag tag : NBTUtils.getChildTag(values, "Items", ListTag.class).getValue()) {
                  if (!(tag instanceof CompoundTag)) {
                     throw new RuntimeException("CompoundTag expected as child tag of Chest's Items");
                  }

                  items.add((CompoundTag)tag);
               }

               this.setItems(this.deserializeInventory(items));
            } catch (InvalidFormatException var7) {
               throw new RuntimeException(var7);
            } catch (DataException var8) {
               throw new RuntimeException(var8);
            }
         } else {
            throw new RuntimeException("'Chest' tile entity expected");
         }
      }
   }
}
