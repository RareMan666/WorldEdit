package com.sk89q.jchronic.handlers;

import com.sk89q.jchronic.tags.Tag;

public class TagPattern extends HandlerPattern {
   private Class<? extends Tag> _tagClass;

   public TagPattern(Class<? extends Tag> tagClass) {
      this(tagClass, false);
   }

   public TagPattern(Class<? extends Tag> tagClass, boolean optional) {
      super(optional);
      this._tagClass = tagClass;
   }

   public Class<? extends Tag> getTagClass() {
      return this._tagClass;
   }

   @Override
   public String toString() {
      return "[TagPattern: tagClass = " + this._tagClass + "]";
   }
}
