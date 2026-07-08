package com.sk89q.worldedit.extension.factory;

import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.pattern.ClipboardPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.internal.registry.InputParser;
import com.sk89q.worldedit.session.ClipboardHolder;

class HashTagPatternParser extends InputParser<Pattern> {
   HashTagPatternParser(WorldEdit worldEdit) {
      super(worldEdit);
   }

   public Pattern parseFromInput(String input, ParserContext context) throws InputParseException {
      if (input.charAt(0) == '#') {
         if (!input.equals("#clipboard") && !input.equals("#copy")) {
            throw new InputParseException("#clipboard or #copy is acceptable for patterns starting with #");
         } else {
            LocalSession session = context.requireSession();
            if (session != null) {
               try {
                  ClipboardHolder holder = session.getClipboard();
                  Clipboard clipboard = holder.getClipboard();
                  return new ClipboardPattern(clipboard);
               } catch (EmptyClipboardException var6) {
                  throw new InputParseException("To use #clipboard, please first copy something to your clipboard");
               }
            } else {
               throw new InputParseException("No session is available, so no clipboard is available");
            }
         }
      } else {
         return null;
      }
   }
}
