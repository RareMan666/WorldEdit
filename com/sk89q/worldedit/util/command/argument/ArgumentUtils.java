package com.sk89q.worldedit.util.command.argument;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;

public final class ArgumentUtils {
   private ArgumentUtils() {
   }

   public static List<String> getMatchingSuggestions(Collection<String> items, String s) {
      if (s.isEmpty()) {
         return Lists.newArrayList(items);
      } else {
         List<String> suggestions = Lists.newArrayList();

         for (String item : items) {
            if (item.toLowerCase().startsWith(s)) {
               suggestions.add(item);
            }
         }

         return suggestions;
      }
   }
}
