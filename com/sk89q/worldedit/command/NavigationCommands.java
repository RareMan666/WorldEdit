package com.sk89q.worldedit.command;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.Logging;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.util.command.parametric.Optional;

public class NavigationCommands {
   private final WorldEdit worldEdit;

   public NavigationCommands(WorldEdit worldEdit) {
      Preconditions.checkNotNull(worldEdit);
      this.worldEdit = worldEdit;
   }

   @Command(aliases = {"unstuck", "!"}, usage = "", desc = "Escape from being stuck inside a block", min = 0, max = 0)
   @CommandPermissions("worldedit.navigation.unstuck")
   public void unstuck(Player player) throws WorldEditException {
      player.print("There you go!");
      player.findFreePosition();
   }

   @Command(aliases = {"ascend", "asc"}, usage = "[# of levels]", desc = "Go up a floor", min = 0, max = 1)
   @CommandPermissions("worldedit.navigation.ascend")
   public void ascend(Player player, @Optional("1") int levelsToAscend) throws WorldEditException {
      int ascentLevels = 1;

      while (player.ascendLevel() && levelsToAscend != ascentLevels) {
         ascentLevels++;
      }

      if (ascentLevels == 0) {
         player.printError("No free spot above you found.");
      } else {
         player.print(ascentLevels != 1 ? "Ascended " + Integer.toString(ascentLevels) + " levels." : "Ascended a level.");
      }
   }

   @Command(aliases = {"descend", "desc"}, usage = "[# of floors]", desc = "Go down a floor", min = 0, max = 1)
   @CommandPermissions("worldedit.navigation.descend")
   public void descend(Player player, @Optional("1") int levelsToDescend) throws WorldEditException {
      int descentLevels = 1;

      while (player.descendLevel() && levelsToDescend != descentLevels) {
         descentLevels++;
      }

      if (descentLevels == 0) {
         player.printError("No free spot above you found.");
      } else {
         player.print(descentLevels != 1 ? "Descended " + Integer.toString(descentLevels) + " levels." : "Descended a level.");
      }
   }

   @Command(aliases = "ceil", usage = "[clearance]", desc = "Go to the celing", flags = "fg", min = 0, max = 1)
   @CommandPermissions("worldedit.navigation.ceiling")
   @Logging(Logging.LogMode.POSITION)
   public void ceiling(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      int clearance = args.argsLength() > 0 ? Math.max(0, args.getInteger(0)) : 0;
      boolean alwaysGlass = this.getAlwaysGlass(args);
      if (player.ascendToCeiling(clearance, alwaysGlass)) {
         player.print("Whoosh!");
      } else {
         player.printError("No free spot above you found.");
      }
   }

   @Command(aliases = "thru", usage = "", desc = "Passthrough walls", min = 0, max = 0)
   @CommandPermissions("worldedit.navigation.thru.command")
   public void thru(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      if (player.passThroughForwardWall(6)) {
         player.print("Whoosh!");
      } else {
         player.printError("No free spot ahead of you found.");
      }
   }

   @Command(aliases = {"jumpto", "j"}, usage = "", desc = "Teleport to a location", min = 0, max = 0)
   @CommandPermissions("worldedit.navigation.jumpto.command")
   public void jumpTo(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      WorldVector pos = player.getSolidBlockTrace(300);
      if (pos != null) {
         player.findFreePosition(pos);
         player.print("Poof!");
      } else {
         player.printError("No block in sight!");
      }
   }

   @Command(aliases = "up", usage = "<block>", desc = "Go upwards some distance", flags = "fg", min = 1, max = 1)
   @CommandPermissions("worldedit.navigation.up")
   @Logging(Logging.LogMode.POSITION)
   public void up(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      int distance = args.getInteger(0);
      boolean alwaysGlass = this.getAlwaysGlass(args);
      if (player.ascendUpwards(distance, alwaysGlass)) {
         player.print("Whoosh!");
      } else {
         player.printError("You would hit something above you.");
      }
   }

   private boolean getAlwaysGlass(CommandContext args) {
      LocalConfiguration config = this.worldEdit.getConfiguration();
      boolean forceFlight = args.hasFlag('f');
      boolean forceGlass = args.hasFlag('g');
      return forceGlass || config.navigationUseGlass && !forceFlight;
   }
}
