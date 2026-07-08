package com.sk89q.worldedit.command.tool.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.command.util.CreatureButcher;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.visitor.EntityVisitor;
import com.sk89q.worldedit.regions.CylinderRegion;
import java.util.List;

public class ButcherBrush implements Brush {
   private CreatureButcher flags;

   public ButcherBrush(CreatureButcher flags) {
      this.flags = flags;
   }

   @Override
   public void build(EditSession editSession, Vector position, Pattern pattern, double size) throws MaxChangedBlocksException {
      CylinderRegion region = CylinderRegion.createRadius(editSession, position, size);
      List<? extends Entity> entities = editSession.getEntities(region);
      Operations.completeLegacy(new EntityVisitor(entities.iterator(), this.flags.createFunction(editSession.getWorld().getWorldData().getEntityRegistry())));
   }
}
