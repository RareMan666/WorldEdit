package com.sk89q.jnbt;

import com.google.common.base.Preconditions;

public class NamedTag {
   private final String name;
   private final Tag tag;

   public NamedTag(String name, Tag tag) {
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(tag);
      this.name = name;
      this.tag = tag;
   }

   public String getName() {
      return this.name;
   }

   public Tag getTag() {
      return this.tag;
   }
}
