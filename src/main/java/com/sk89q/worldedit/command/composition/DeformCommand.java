package com.sk89q.worldedit.command.composition;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.minecraft.util.commands.WrappedCommandException;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.command.argument.BooleanFlag;
import com.sk89q.worldedit.command.argument.StringParser;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.function.Contextual;
import com.sk89q.worldedit.function.factory.Deform;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.composition.FlagParser;
import com.sk89q.worldedit.util.command.composition.SimpleCommand;

public class DeformCommand extends SimpleCommand<Contextual<? extends Operation>> {
   private final FlagParser.Flag<Boolean> rawCoordsFlag = this.addFlag('r', new BooleanFlag("Raw coords mode"));
   private final FlagParser.Flag<Boolean> offsetFlag = this.addFlag('o', new BooleanFlag("Offset mode"));
   private final StringParser expressionParser = this.addParameter(new StringParser("expression", "Expression to apply", "y-=0.2"));

   public Deform call(CommandArgs args, CommandLocals locals) throws CommandException {
      FlagParser.FlagData flagData = this.getFlagParser().call(args, locals);
      String expression = this.expressionParser.call(args, locals);
      boolean rawCoords = this.rawCoordsFlag.get(flagData, false);
      boolean offset = this.offsetFlag.get(flagData, false);
      Deform deform = new Deform(expression);
      if (rawCoords) {
         deform.setMode(Deform.Mode.RAW_COORD);
      } else if (offset) {
         deform.setMode(Deform.Mode.OFFSET);
         Player player = (Player)locals.get(Actor.class);
         LocalSession session = WorldEdit.getInstance().getSessionManager().get(locals.get(Actor.class));

         try {
            deform.setOffset(session.getPlacementPosition(player));
         } catch (IncompleteRegionException var11) {
            throw new WrappedCommandException(var11);
         }
      } else {
         deform.setMode(Deform.Mode.UNIT_CUBE);
      }

      return deform;
   }

   @Override
   public String getDescription() {
      return "Apply math expression to area";
   }

   @Override
   protected boolean testPermission0(CommandLocals locals) {
      return true;
   }
}
