package com.sk89q.worldedit.util.command.binding;

import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.worldedit.util.command.parametric.ArgumentStack;
import com.sk89q.worldedit.util.command.parametric.BindingBehavior;
import com.sk89q.worldedit.util.command.parametric.BindingHelper;
import com.sk89q.worldedit.util.command.parametric.BindingMatch;

public final class StandardBindings extends BindingHelper {
   @BindingMatch(type = CommandContext.class, behavior = BindingBehavior.PROVIDES)
   public CommandContext getCommandContext(ArgumentStack context) {
      context.markConsumed();
      return context.getContext();
   }
}
