package com.sk89q.worldedit.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.ItemType;
import com.sk89q.worldedit.command.tool.BlockDataCyler;
import com.sk89q.worldedit.command.tool.BlockReplacer;
import com.sk89q.worldedit.command.tool.DistanceWand;
import com.sk89q.worldedit.command.tool.FloatingTreeRemover;
import com.sk89q.worldedit.command.tool.FloodFillTool;
import com.sk89q.worldedit.command.tool.LongRangeBuildTool;
import com.sk89q.worldedit.command.tool.QueryTool;
import com.sk89q.worldedit.command.tool.TreePlanter;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.util.TreeGenerator;

public class ToolCommands {
   private final WorldEdit we;

   public ToolCommands(WorldEdit we) {
      this.we = we;
   }

   @Command(aliases = "none", usage = "", desc = "Unbind a bound tool from your current item", min = 0, max = 0)
   public void none(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      session.setTool(player.getItemInHand(), null);
      player.print("Tool unbound from your current item.");
   }

   @Command(aliases = "info", usage = "", desc = "Block information tool", min = 0, max = 0)
   @CommandPermissions("worldedit.tool.info")
   public void info(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      session.setTool(player.getItemInHand(), new QueryTool());
      player.print("Info tool bound to " + ItemType.toHeldName(player.getItemInHand()) + ".");
   }

   @Command(aliases = "tree", usage = "[type]", desc = "Tree generator tool", min = 0, max = 1)
   @CommandPermissions("worldedit.tool.tree")
   public void tree(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      TreeGenerator.TreeType type = args.argsLength() > 0 ? TreeGenerator.lookup(args.getString(0)) : TreeGenerator.TreeType.TREE;
      if (type == null) {
         player.printError("Tree type '" + args.getString(0) + "' is unknown.");
      } else {
         session.setTool(player.getItemInHand(), new TreePlanter(new TreeGenerator(type)));
         player.print("Tree tool bound to " + ItemType.toHeldName(player.getItemInHand()) + ".");
      }
   }

   @Command(aliases = "repl", usage = "<block>", desc = "Block replacer tool", min = 1, max = 1)
   @CommandPermissions("worldedit.tool.replacer")
   public void repl(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      BaseBlock targetBlock = this.we.getBlock(player, args.getString(0));
      session.setTool(player.getItemInHand(), new BlockReplacer(targetBlock));
      player.print("Block replacer tool bound to " + ItemType.toHeldName(player.getItemInHand()) + ".");
   }

   @Command(aliases = "cycler", usage = "", desc = "Block data cycler tool", min = 0, max = 0)
   @CommandPermissions("worldedit.tool.data-cycler")
   public void cycler(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      session.setTool(player.getItemInHand(), new BlockDataCyler());
      player.print("Block data cycler tool bound to " + ItemType.toHeldName(player.getItemInHand()) + ".");
   }

   @Command(aliases = {"floodfill", "flood"}, usage = "<pattern> <range>", desc = "Flood fill tool", min = 2, max = 2)
   @CommandPermissions("worldedit.tool.flood-fill")
   public void floodFill(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      LocalConfiguration config = this.we.getConfiguration();
      int range = args.getInteger(1);
      if (range > config.maxSuperPickaxeSize) {
         player.printError("Maximum range: " + config.maxSuperPickaxeSize);
      } else {
         Pattern pattern = this.we.getBlockPattern(player, args.getString(0));
         session.setTool(player.getItemInHand(), new FloodFillTool(range, pattern));
         player.print("Block flood fill tool bound to " + ItemType.toHeldName(player.getItemInHand()) + ".");
      }
   }

   @Command(aliases = "deltree", usage = "", desc = "Floating tree remover tool", min = 0, max = 0)
   @CommandPermissions("worldedit.tool.deltree")
   public void deltree(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      session.setTool(player.getItemInHand(), new FloatingTreeRemover());
      player.print("Floating tree remover tool bound to " + ItemType.toHeldName(player.getItemInHand()) + ".");
   }

   @Command(aliases = "farwand", usage = "", desc = "Wand at a distance tool", min = 0, max = 0)
   @CommandPermissions("worldedit.tool.farwand")
   public void farwand(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      session.setTool(player.getItemInHand(), new DistanceWand());
      player.print("Far wand tool bound to " + ItemType.toHeldName(player.getItemInHand()) + ".");
   }

   @Command(aliases = {"lrbuild", "/lrbuild"}, usage = "<leftclick block> <rightclick block>", desc = "Long-range building tool", min = 2, max = 2)
   @CommandPermissions("worldedit.tool.lrbuild")
   public void longrangebuildtool(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      BaseBlock secondary = this.we.getBlock(player, args.getString(0));
      BaseBlock primary = this.we.getBlock(player, args.getString(1));
      session.setTool(player.getItemInHand(), new LongRangeBuildTool(primary, secondary));
      player.print("Long-range building tool bound to " + ItemType.toHeldName(player.getItemInHand()) + ".");
      player.print("Left-click set to " + ItemType.toName(secondary.getType()) + "; right-click set to " + ItemType.toName(primary.getType()) + ".");
   }
}
