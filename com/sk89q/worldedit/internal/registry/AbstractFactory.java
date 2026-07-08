package com.sk89q.worldedit.internal.registry;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.NoMatchException;
import com.sk89q.worldedit.extension.input.ParserContext;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFactory<E> {
   protected final WorldEdit worldEdit;
   protected final List<InputParser<E>> parsers = new ArrayList<>();

   protected AbstractFactory(WorldEdit worldEdit) {
      Preconditions.checkNotNull(worldEdit);
      this.worldEdit = worldEdit;
   }

   public E parseFromInput(String input, ParserContext context) throws InputParseException {
      for (InputParser<E> parser : this.parsers) {
         E match = parser.parseFromInput(input, context);
         if (match != null) {
            return match;
         }
      }

      throw new NoMatchException("No match for '" + input + "'");
   }
}
