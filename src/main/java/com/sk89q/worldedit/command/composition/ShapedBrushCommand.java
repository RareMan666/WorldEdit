package com.sk89q.worldedit.command.composition;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxBrushRadiusException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.command.argument.NumberParser;
import com.sk89q.worldedit.command.argument.RegionFactoryParser;
import com.sk89q.worldedit.command.tool.BrushTool;
import com.sk89q.worldedit.command.tool.InvalidToolBindException;
import com.sk89q.worldedit.command.tool.brush.OperationFactoryBrush;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.function.Contextual;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.regions.factory.RegionFactory;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.composition.CommandExecutor;
import com.sk89q.worldedit.util.command.composition.SimpleCommand;

public class ShapedBrushCommand extends SimpleCommand<Object> {
   private final CommandExecutor<? extends Contextual<? extends Operation>> delegate;
   private final String permission;
   private final RegionFactoryParser regionFactoryParser = this.addParameter(new RegionFactoryParser());
   private final NumberParser radiusCommand = this.addParameter(new NumberParser("size", "The size of the brush", "5"));

   public ShapedBrushCommand(CommandExecutor<? extends Contextual<? extends Operation>> delegate, String permission) {
      Preconditions.checkNotNull(delegate, "delegate");
      this.permission = permission;
      this.delegate = delegate;
      this.addParameter(delegate);
   }

   @Override
   public Object call(CommandArgs args, CommandLocals locals) throws CommandException {
      if (!this.testPermission(locals)) {
         throw new CommandPermissionsException();
      } else {
         RegionFactory regionFactory = this.regionFactoryParser.call(args, locals);
         int radius = this.radiusCommand.call(args, locals).intValue();
         Contextual<? extends Operation> factory = (Contextual<? extends Operation>)this.delegate.call(args, locals);
         Player player = (Player)locals.get(Actor.class);
         LocalSession session = WorldEdit.getInstance().getSessionManager().get(player);

         try {
            WorldEdit.getInstance().checkMaxBrushRadius(radius);
            BrushTool tool = session.getBrushTool(player.getItemInHand());
            tool.setSize(radius);
            tool.setBrush(new OperationFactoryBrush(factory, regionFactory), this.permission);
         } catch (MaxBrushRadiusException var9) {
            WorldEdit.getInstance().getPlatformManager().getCommandManager().getExceptionConverter().convert(var9);
         } catch (InvalidToolBindException var10) {
            WorldEdit.getInstance().getPlatformManager().getCommandManager().getExceptionConverter().convert(var10);
         }

         player.print("Set brush to " + factory);
         return true;
      }
   }

   @Override
   public String getDescription() {
      return this.delegate.getDescription();
   }

   @Override
   public boolean testPermission0(CommandLocals locals) {
      Actor sender = locals.get(Actor.class);
      if (sender == null) {
         throw new RuntimeException("Uh oh! No 'Actor' specified so that we can check permissions");
      } else {
         return sender.hasPermission(this.permission);
      }
   }
}
