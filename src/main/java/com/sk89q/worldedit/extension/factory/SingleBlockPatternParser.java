package com.sk89q.worldedit.extension.factory;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.internal.registry.InputParser;

class SingleBlockPatternParser extends InputParser<Pattern> {
   SingleBlockPatternParser(WorldEdit worldEdit) {
      super(worldEdit);
   }

   public Pattern parseFromInput(String input, ParserContext context) throws InputParseException {
      String[] items = input.split(",");
      return items.length == 1 ? new BlockPattern(this.worldEdit.getBlockFactory().parseFromInput(items[0], context)) : null;
   }
}
