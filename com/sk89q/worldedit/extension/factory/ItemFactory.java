package com.sk89q.worldedit.extension.factory;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseItem;
import com.sk89q.worldedit.internal.registry.AbstractFactory;

public class ItemFactory extends AbstractFactory<BaseItem> {
   public ItemFactory(WorldEdit worldEdit) {
      super(worldEdit);
      this.parsers.add(new DefaultItemParser(worldEdit));
   }
}
