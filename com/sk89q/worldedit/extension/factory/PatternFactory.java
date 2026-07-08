package com.sk89q.worldedit.extension.factory;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.internal.registry.AbstractFactory;

public final class PatternFactory extends AbstractFactory<Pattern> {
   public PatternFactory(WorldEdit worldEdit) {
      super(worldEdit);
      this.parsers.add(new HashTagPatternParser(worldEdit));
      this.parsers.add(new SingleBlockPatternParser(worldEdit));
      this.parsers.add(new RandomPatternParser(worldEdit));
   }
}
