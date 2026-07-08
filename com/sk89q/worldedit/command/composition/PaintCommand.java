package com.sk89q.worldedit.command.composition;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.command.argument.NumberParser;
import com.sk89q.worldedit.command.argument.RegionFunctionParser;
import com.sk89q.worldedit.function.Contextual;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.factory.Paint;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.composition.CommandExecutor;
import com.sk89q.worldedit.util.command.composition.SimpleCommand;

public class PaintCommand extends SimpleCommand<Paint> {
   private final NumberParser densityCommand = this.addParameter(new NumberParser("density", "0-100", "20"));
   private final CommandExecutor<? extends Contextual<? extends RegionFunction>> functionParser;

   public PaintCommand() {
      this(new RegionFunctionParser());
   }

   public PaintCommand(CommandExecutor<? extends Contextual<? extends RegionFunction>> functionParser) {
      this.functionParser = functionParser;
      this.addParameter(functionParser);
   }

   public Paint call(CommandArgs args, CommandLocals locals) throws CommandException {
      double density = this.densityCommand.call(args, locals).doubleValue() / 100.0;
      Contextual<? extends RegionFunction> function = (Contextual<? extends RegionFunction>)this.functionParser.call(args, locals);
      return new Paint(function, density);
   }

   @Override
   public String getDescription() {
      return "Applies a function to surfaces";
   }

   @Override
   protected boolean testPermission0(CommandLocals locals) {
      return true;
   }
}
