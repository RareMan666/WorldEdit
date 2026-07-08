package com.sk89q.worldedit.util.formatting.component;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.extension.platform.CommandManager;
import com.sk89q.worldedit.util.command.CommandCallable;
import com.sk89q.worldedit.util.command.CommandMapping;
import com.sk89q.worldedit.util.command.Description;
import com.sk89q.worldedit.util.command.Dispatcher;
import com.sk89q.worldedit.util.command.PrimaryAliasComparator;
import com.sk89q.worldedit.util.formatting.StyledFragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public class CommandUsageBox extends StyledFragment {
   public CommandUsageBox(CommandCallable command, String commandString) {
      this(command, commandString, null);
   }

   public CommandUsageBox(CommandCallable command, String commandString, @Nullable CommandLocals locals) {
      Preconditions.checkNotNull(command);
      Preconditions.checkNotNull(commandString);
      if (command instanceof Dispatcher) {
         this.attachDispatcherUsage((Dispatcher)command, commandString, locals);
      } else {
         this.attachCommandUsage(command.getDescription(), commandString);
      }
   }

   private void attachDispatcherUsage(Dispatcher dispatcher, String commandString, @Nullable CommandLocals locals) {
      CommandListBox box = new CommandListBox("Subcommands");
      String prefix = !commandString.isEmpty() ? commandString + " " : "";
      List<CommandMapping> list = new ArrayList<>(dispatcher.getCommands());
      Collections.sort(list, new PrimaryAliasComparator(CommandManager.COMMAND_CLEAN_PATTERN));

      for (CommandMapping mapping : list) {
         if (locals == null || mapping.getCallable().testPermission(locals)) {
            box.appendCommand(prefix + mapping.getPrimaryAlias(), mapping.getDescription().getDescription());
         }
      }

      this.append(box);
   }

   private void attachCommandUsage(Description description, String commandString) {
      MessageBox box = new MessageBox("Help for " + commandString);
      StyledFragment contents = box.getContents();
      if (description.getUsage() != null) {
         contents.append(new Label().append("Usage: "));
         contents.append(description.getUsage());
      } else {
         contents.append(new Subtle().append("Usage information is not available."));
      }

      contents.newLine();
      if (description.getHelp() != null) {
         contents.append(description.getHelp());
      } else if (description.getDescription() != null) {
         contents.append(description.getDescription());
      } else {
         contents.append(new Subtle().append("No further help is available."));
      }

      this.append(box);
   }
}
