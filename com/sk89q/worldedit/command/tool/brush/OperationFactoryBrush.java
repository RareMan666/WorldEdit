package com.sk89q.worldedit.command.tool.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.function.Contextual;
import com.sk89q.worldedit.function.EditContext;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.regions.factory.RegionFactory;

public class OperationFactoryBrush implements Brush {
   private final Contextual<? extends Operation> operationFactory;
   private final RegionFactory regionFactory;

   public OperationFactoryBrush(Contextual<? extends Operation> operationFactory, RegionFactory regionFactory) {
      this.operationFactory = operationFactory;
      this.regionFactory = regionFactory;
   }

   @Override
   public void build(EditSession editSession, Vector position, Pattern pattern, double size) throws MaxChangedBlocksException {
      EditContext context = new EditContext();
      context.setDestination(editSession);
      context.setRegion(this.regionFactory.createCenteredAt(position, size));
      context.setFill(pattern);
      Operation operation = this.operationFactory.createFromContext(context);
      Operations.completeLegacy(operation);
   }
}
