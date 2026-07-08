package com.sk89q.worldedit.history.change;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.history.UndoContext;
import com.sk89q.worldedit.util.Location;

public class EntityRemove implements Change {
   private final Location location;
   private final BaseEntity state;
   private Entity entity;

   public EntityRemove(Location location, BaseEntity state) {
      Preconditions.checkNotNull(location);
      Preconditions.checkNotNull(state);
      this.location = location;
      this.state = state;
   }

   @Override
   public void undo(UndoContext context) throws WorldEditException {
      this.entity = ((Extent)Preconditions.checkNotNull(context.getExtent())).createEntity(this.location, this.state);
   }

   @Override
   public void redo(UndoContext context) throws WorldEditException {
      if (this.entity != null) {
         this.entity.remove();
         this.entity = null;
      }
   }
}
