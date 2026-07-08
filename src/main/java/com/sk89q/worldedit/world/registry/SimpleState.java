package com.sk89q.worldedit.world.registry;

import com.sk89q.worldedit.blocks.BaseBlock;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nullable;

public class SimpleState implements State {
   private Byte dataMask;
   private Map<String, SimpleStateValue> values;

   @Override
   public Map<String, SimpleStateValue> valueMap() {
      return Collections.unmodifiableMap(this.values);
   }

   @Nullable
   @Override
   public StateValue getValue(BaseBlock block) {
      for (StateValue value : this.values.values()) {
         if (value.isSet(block)) {
            return value;
         }
      }

      return null;
   }

   public byte getDataMask() {
      return this.dataMask != null ? this.dataMask : 15;
   }

   @Override
   public boolean hasDirection() {
      for (SimpleStateValue value : this.values.values()) {
         if (value.getDirection() != null) {
            return true;
         }
      }

      return false;
   }

   void postDeserialization() {
      for (SimpleStateValue v : this.values.values()) {
         v.setState(this);
      }
   }
}
