package com.sk89q.worldedit.blocks;

import java.util.HashMap;
import java.util.Map;

public class BaseItem {
   private int id;
   private short data;
   private final Map<Integer, Integer> enchantments = new HashMap<>();

   public BaseItem(int id) {
      this.id = id;
      this.data = 0;
   }

   public BaseItem(int id, short data) {
      this.id = id;
      this.data = data;
   }

   public int getType() {
      return this.id;
   }

   public void setType(int id) {
      this.id = id;
   }

   @Deprecated
   public short getDamage() {
      return this.data;
   }

   public short getData() {
      return this.data;
   }

   @Deprecated
   public void setDamage(short data) {
      this.data = data;
   }

   public void setData(short data) {
      this.data = data;
   }

   public Map<Integer, Integer> getEnchantments() {
      return this.enchantments;
   }
}
