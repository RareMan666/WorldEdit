package com.sk89q.worldedit.command.composition;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.command.argument.RegionFunctionParser;
import com.sk89q.worldedit.function.Contextual;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.factory.Apply;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.composition.CommandExecutor;
import com.sk89q.worldedit.util.command.composition.SimpleCommand;

public class ApplyCommand extends SimpleCommand<Contextual<? extends Operation>> {
   private final CommandExecutor<Contextual<? extends RegionFunction>> functionParser;
   private final String description;

   public ApplyCommand() {
      this(new RegionFunctionParser(), "Applies a function to every block");
   }

   public ApplyCommand(CommandExecutor<Contextual<? extends RegionFunction>> functionParser, String description) {
      Preconditions.checkNotNull(functionParser, "functionParser");
      Preconditions.checkNotNull(description, "description");
      this.functionParser = functionParser;
      this.description = description;
      this.addParameter(functionParser);
   }

   public Apply call(CommandArgs args, CommandLocals locals) throws CommandException {
      Contextual<? extends RegionFunction> function = this.functionParser.call(args, locals);
      return new Apply(function);
   }

   @Override
   public String getDescription() {
      return this.description;
   }

   @Override
   protected boolean testPermission0(CommandLocals locals) {
      return true;
   }
}
