package com.sk89q.worldedit.command;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Player;

public class HistoryCommands {
   private final WorldEdit worldEdit;

   public HistoryCommands(WorldEdit worldEdit) {
      Preconditions.checkNotNull(worldEdit);
      this.worldEdit = worldEdit;
   }

   @Command(aliases = {"/undo", "undo"}, usage = "[times] [player]", desc = "Undoes the last action", min = 0, max = 2)
   @CommandPermissions("worldedit.history.undo")
   public void undo(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      int times = Math.max(1, args.getInteger(0, 1));

      for (int i = 0; i < times; i++) {
         EditSession undone;
         if (args.argsLength() < 2) {
            undone = session.undo(session.getBlockBag(player), player);
         } else {
            player.checkPermission("worldedit.history.undo.other");
            LocalSession sess = this.worldEdit.getSession(args.getString(1));
            if (sess == null) {
               player.printError("Unable to find session for " + args.getString(1));
               break;
            }

            undone = sess.undo(session.getBlockBag(player), player);
         }

         if (undone == null) {
            player.printError("Nothing left to undo.");
            break;
         }

         player.print("Undo successful.");
         this.worldEdit.flushBlockBag(player, undone);
      }
   }

   @Command(aliases = {"/redo", "redo"}, usage = "[times] [player]", desc = "Redoes the last action (from history)", min = 0, max = 2)
   @CommandPermissions("worldedit.history.redo")
   public void redo(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      int times = Math.max(1, args.getInteger(0, 1));

      for (int i = 0; i < times; i++) {
         EditSession redone;
         if (args.argsLength() < 2) {
            redone = session.redo(session.getBlockBag(player), player);
         } else {
            player.checkPermission("worldedit.history.redo.other");
            LocalSession sess = this.worldEdit.getSession(args.getString(1));
            if (sess == null) {
               player.printError("Unable to find session for " + args.getString(1));
               break;
            }

            redone = sess.redo(session.getBlockBag(player), player);
         }

         if (redone != null) {
            player.print("Redo successful.");
            this.worldEdit.flushBlockBag(player, redone);
         } else {
            player.printError("Nothing left to redo.");
         }
      }
   }

   @Command(aliases = {"/clearhistory", "clearhistory"}, usage = "", desc = "Clear your history", min = 0, max = 0)
   @CommandPermissions("worldedit.history.clear")
   public void clearHistory(Player player, LocalSession session, EditSession editSession) throws WorldEditException {
      session.clearHistory();
      player.print("History cleared.");
   }
}
