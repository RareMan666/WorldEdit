package com.sk89q.worldedit.util.command;

import java.util.Comparator;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public final class PrimaryAliasComparator implements Comparator<CommandMapping> {
   public static final PrimaryAliasComparator INSTANCE = new PrimaryAliasComparator(null);
   @Nullable
   private final Pattern removalPattern;

   public PrimaryAliasComparator(@Nullable Pattern removalPattern) {
      this.removalPattern = removalPattern;
   }

   private String clean(String alias) {
      return this.removalPattern != null ? this.removalPattern.matcher(alias).replaceAll("") : alias;
   }

   public int compare(CommandMapping o1, CommandMapping o2) {
      return this.clean(o1.getPrimaryAlias()).compareTo(this.clean(o2.getPrimaryAlias()));
   }
}
