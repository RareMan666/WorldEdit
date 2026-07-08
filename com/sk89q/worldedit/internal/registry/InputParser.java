package com.sk89q.worldedit.internal.registry;

import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;

public abstract class InputParser<E> {
   protected final WorldEdit worldEdit;

   protected InputParser(WorldEdit worldEdit) {
      this.worldEdit = worldEdit;
   }

   public abstract E parseFromInput(String var1, ParserContext var2) throws InputParseException;
}
