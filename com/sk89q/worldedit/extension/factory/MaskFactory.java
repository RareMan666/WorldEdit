package com.sk89q.worldedit.extension.factory;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.internal.registry.AbstractFactory;

public final class MaskFactory extends AbstractFactory<Mask> {
   public MaskFactory(WorldEdit worldEdit) {
      super(worldEdit);
      this.parsers.add(new DefaultMaskParser(worldEdit));
   }
}
