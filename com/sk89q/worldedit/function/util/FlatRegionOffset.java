package com.sk89q.worldedit.function.util;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.FlatRegionFunction;

public class FlatRegionOffset implements FlatRegionFunction {
   private Vector2D offset;
   private final FlatRegionFunction function;

   public FlatRegionOffset(Vector2D offset, FlatRegionFunction function) {
      Preconditions.checkNotNull(function);
      this.setOffset(offset);
      this.function = function;
   }

   public Vector2D getOffset() {
      return this.offset;
   }

   public void setOffset(Vector2D offset) {
      Preconditions.checkNotNull(offset);
      this.offset = offset;
   }

   @Override
   public boolean apply(Vector2D position) throws WorldEditException {
      return this.function.apply(position.add(this.offset));
   }
}
