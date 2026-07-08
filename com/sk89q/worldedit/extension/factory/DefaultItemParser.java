package com.sk89q.worldedit.extension.factory;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseItem;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.internal.registry.InputParser;

public class DefaultItemParser extends InputParser<BaseItem> {
   protected DefaultItemParser(WorldEdit worldEdit) {
      super(worldEdit);
   }

   public BaseItem parseFromInput(String input, ParserContext context) throws InputParseException {
      String[] tokens = input.split(":", 3);
      short meta = 0;

      BaseItem item;
      try {
         int id = Integer.parseInt(tokens[0]);
         if (tokens.length == 2) {
            try {
               meta = Short.parseShort(tokens[1]);
            } catch (NumberFormatException var10) {
               throw new InputParseException("Expected '" + tokens[1] + "' to be a metadata value but it's not a number");
            }
         }

         item = context.requireWorld().getWorldData().getItemRegistry().createFromId(id);
      } catch (NumberFormatException var11) {
         if (input.length() < 2) {
            throw new InputParseException("'" + input + "' isn't a known item name format");
         }

         String name = tokens[0] + ":" + tokens[1];
         if (tokens.length == 3) {
            try {
               meta = Short.parseShort(tokens[2]);
            } catch (NumberFormatException var9) {
               throw new InputParseException("Expected '" + tokens[2] + "' to be a metadata value but it's not a number");
            }
         }

         item = context.requireWorld().getWorldData().getItemRegistry().createFromId(name);
      }

      if (item == null) {
         throw new InputParseException("'" + input + "' did not match any item");
      } else {
         item.setData(meta);
         return item;
      }
   }
}
