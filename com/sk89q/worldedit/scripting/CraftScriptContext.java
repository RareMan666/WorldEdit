package com.sk89q.worldedit.scripting;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.command.InsufficientArgumentsException;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.patterns.Pattern;
import com.sk89q.worldedit.util.io.file.FilenameException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CraftScriptContext extends CraftScriptEnvironment {
   private List<EditSession> editSessions = new ArrayList<>();
   private String[] args;

   public CraftScriptContext(WorldEdit controller, Platform server, LocalConfiguration config, LocalSession session, Player player, String[] args) {
      super(controller, server, config, session, player);
      this.args = args;
   }

   public EditSession remember() {
      EditSession editSession = this.controller
         .getEditSessionFactory()
         .getEditSession(this.player.getWorld(), this.session.getBlockChangeLimit(), this.session.getBlockBag(this.player), this.player);
      editSession.enableQueue();
      this.editSessions.add(editSession);
      return editSession;
   }

   public Player getPlayer() {
      return this.player;
   }

   public LocalSession getSession() {
      return this.session;
   }

   public LocalConfiguration getConfiguration() {
      return this.config;
   }

   public List<EditSession> getEditSessions() {
      return Collections.unmodifiableList(this.editSessions);
   }

   public void print(String message) {
      this.player.print(message);
   }

   public void error(String message) {
      this.player.printError(message);
   }

   public void printRaw(String message) {
      this.player.printRaw(message);
   }

   public void checkArgs(int min, int max, String usage) throws InsufficientArgumentsException {
      if (this.args.length <= min || max != -1 && this.args.length - 1 > max) {
         throw new InsufficientArgumentsException("Usage: " + usage);
      }
   }

   public BaseBlock getBlock(String input, boolean allAllowed) throws WorldEditException {
      return this.controller.getBlock(this.player, input, allAllowed);
   }

   public BaseBlock getBlock(String id) throws WorldEditException {
      return this.controller.getBlock(this.player, id, false);
   }

   public Pattern getBlockPattern(String list) throws WorldEditException {
      return this.controller.getBlockPattern(this.player, list);
   }

   public Set<Integer> getBlockIDs(String list, boolean allBlocksAllowed) throws WorldEditException {
      return this.controller.getBlockIDs(this.player, list, allBlocksAllowed);
   }

   @Deprecated
   public File getSafeFile(String folder, String filename) throws FilenameException {
      File dir = this.controller.getWorkingDirectoryFile(folder);
      return this.controller.getSafeOpenFile(this.player, dir, filename, null, (String[])null);
   }

   public File getSafeOpenFile(String folder, String filename, String defaultExt, String... exts) throws FilenameException {
      File dir = this.controller.getWorkingDirectoryFile(folder);
      return this.controller.getSafeOpenFile(this.player, dir, filename, defaultExt, exts);
   }

   public File getSafeSaveFile(String folder, String filename, String defaultExt, String... exts) throws FilenameException {
      File dir = this.controller.getWorkingDirectoryFile(folder);
      return this.controller.getSafeSaveFile(this.player, dir, filename, defaultExt, exts);
   }
}
