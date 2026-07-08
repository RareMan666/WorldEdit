package com.sk89q.worldedit.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.util.command.parametric.Optional;

public class ToolUtilCommands {
   private final WorldEdit we;

   public ToolUtilCommands(WorldEdit we) {
      this.we = we;
   }

   @Command(aliases = {"/", ","}, usage = "[on|off]", desc = "Toggle the super pickaxe function", min = 0, max = 1)
   @CommandPermissions("worldedit.superpickaxe")
   public void togglePickaxe(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      String newState = args.getString(0, null);
      if (session.hasSuperPickAxe()) {
         if ("on".equals(newState)) {
            player.printError("Super pick axe already enabled.");
            return;
         }

         session.disableSuperPickAxe();
         player.print("Super pick axe disabled.");
      } else {
         if ("off".equals(newState)) {
            player.printError("Super pick axe already disabled.");
            return;
         }

         session.enableSuperPickAxe();
         player.print("Super pick axe enabled.");
      }
   }

   @Command(aliases = "mask", usage = "[mask]", desc = "Set the brush mask", min = 0, max = -1)
   @CommandPermissions("worldedit.brush.options.mask")
   public void mask(Player player, LocalSession session, EditSession editSession, @Optional Mask mask) throws WorldEditException {
      if (mask == null) {
         session.getBrushTool(player.getItemInHand()).setMask(null);
         player.print("Brush mask disabled.");
      } else {
         session.getBrushTool(player.getItemInHand()).setMask(mask);
         player.print("Brush mask set.");
      }
   }

   @Command(aliases = {"mat", "material"}, usage = "[pattern]", desc = "Set the brush material", min = 1, max = 1)
   @CommandPermissions("worldedit.brush.options.material")
   public void material(Player player, LocalSession session, EditSession editSession, Pattern pattern) throws WorldEditException {
      session.getBrushTool(player.getItemInHand()).setFill(pattern);
      player.print("Brush material set.");
   }

   @Command(aliases = "range", usage = "[pattern]", desc = "Set the brush range", min = 1, max = 1)
   @CommandPermissions("worldedit.brush.options.range")
   public void range(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      int range = args.getInteger(0);
      session.getBrushTool(player.getItemInHand()).setRange(range);
      player.print("Brush range set.");
   }

   @Command(aliases = "size", usage = "[pattern]", desc = "Set the brush size", min = 1, max = 1)
   @CommandPermissions("worldedit.brush.options.size")
   public void size(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      int radius = args.getInteger(0);
      this.we.checkMaxBrushRadius(radius);
      session.getBrushTool(player.getItemInHand()).setSize(radius);
      player.print("Brush size set.");
   }
}
