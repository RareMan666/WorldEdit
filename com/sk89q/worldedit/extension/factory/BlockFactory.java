package com.sk89q.worldedit.extension.factory;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.internal.registry.AbstractFactory;
import java.util.HashSet;
import java.util.Set;

public class BlockFactory extends AbstractFactory<BaseBlock> {
   public BlockFactory(WorldEdit worldEdit) {
      super(worldEdit);
      this.parsers.add(new DefaultBlockParser(worldEdit));
   }

   public Set<BaseBlock> parseFromListInput(String input, ParserContext context) throws InputParseException {
      Set<BaseBlock> blocks = new HashSet<>();

      for (String token : input.split(",")) {
         blocks.add(this.parseFromInput(token, context));
      }

      return blocks;
   }
}
