package com.sk89q.worldedit.extension.factory;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import com.sk89q.worldedit.internal.registry.InputParser;

class RandomPatternParser extends InputParser<Pattern> {
   RandomPatternParser(WorldEdit worldEdit) {
      super(worldEdit);
   }

   public Pattern parseFromInput(String input, ParserContext context) throws InputParseException {
      BlockFactory blockRegistry = this.worldEdit.getBlockFactory();
      RandomPattern randomPattern = new RandomPattern();

      for (String token : input.split(",")) {
         double chance;
         BaseBlock block;
         if (token.matches("[0-9]+(\\.[0-9]*)?%.*")) {
            String[] p = token.split("%");
            if (p.length < 2) {
               throw new InputParseException("Missing the type after the % symbol for '" + input + "'");
            }

            chance = Double.parseDouble(p[0]);
            block = blockRegistry.parseFromInput(p[1], context);
         } else {
            chance = 1.0;
            block = blockRegistry.parseFromInput(token, context);
         }

         randomPattern.add(new BlockPattern(block), chance);
      }

      return randomPattern;
   }
}
