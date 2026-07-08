package com.sk89q.worldedit.command.argument;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.extent.NullExtent;
import com.sk89q.worldedit.function.Contextual;
import com.sk89q.worldedit.function.EditContext;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.block.BlockReplace;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.util.GuavaUtil;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.composition.SimpleCommand;

public class ReplaceParser extends SimpleCommand<Contextual<? extends RegionFunction>> {
   private final PatternParser fillArg = this.addParameter(new PatternParser("fillPattern"));

   public Contextual<RegionFunction> call(CommandArgs args, CommandLocals locals) throws CommandException {
      Pattern fill = this.fillArg.call(args, locals);
      return new ReplaceParser.ReplaceFactory(fill);
   }

   @Override
   public String getDescription() {
      return "Replaces blocks";
   }

   @Override
   protected boolean testPermission0(CommandLocals locals) {
      return true;
   }

   private static class ReplaceFactory implements Contextual<RegionFunction> {
      private final Pattern fill;

      private ReplaceFactory(Pattern fill) {
         this.fill = fill;
      }

      public RegionFunction createFromContext(EditContext context) {
         return new BlockReplace(GuavaUtil.firstNonNull(context.getDestination(), new NullExtent()), GuavaUtil.firstNonNull(context.getFill(), this.fill));
      }

      @Override
      public String toString() {
         return "replace blocks";
      }
   }
}
