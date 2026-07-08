package com.sk89q.worldedit.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.Logging;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.snapshot.InvalidSnapshotException;
import com.sk89q.worldedit.world.snapshot.Snapshot;
import com.sk89q.worldedit.world.snapshot.SnapshotRestore;
import com.sk89q.worldedit.world.storage.ChunkStore;
import com.sk89q.worldedit.world.storage.MissingWorldException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class SnapshotUtilCommands {
   private static final Logger logger = Logger.getLogger("Minecraft.WorldEdit");
   private final WorldEdit we;

   public SnapshotUtilCommands(WorldEdit we) {
      this.we = we;
   }

   @Command(aliases = {"restore", "/restore"}, usage = "[snapshot]", desc = "Restore the selection from a snapshot", min = 0, max = 1)
   @Logging(Logging.LogMode.REGION)
   @CommandPermissions("worldedit.snapshots.restore")
   public void restore(Player player, LocalSession session, EditSession editSession, CommandContext args) throws WorldEditException {
      LocalConfiguration config = this.we.getConfiguration();
      if (config.snapshotRepo == null) {
         player.printError("Snapshot/backup restore is not configured.");
      } else {
         Region region = session.getSelection(player.getWorld());
         Snapshot snapshot;
         if (args.argsLength() > 0) {
            try {
               snapshot = config.snapshotRepo.getSnapshot(args.getString(0));
            } catch (InvalidSnapshotException var27) {
               player.printError("That snapshot does not exist or is not available.");
               return;
            }
         } else {
            snapshot = session.getSnapshot();
         }

         if (snapshot == null) {
            try {
               snapshot = config.snapshotRepo.getDefaultSnapshot(player.getWorld().getName());
               if (snapshot == null) {
                  player.printError("No snapshots were found. See console for details.");
                  File dir = config.snapshotRepo.getDirectory();

                  try {
                     logger.info("WorldEdit found no snapshots: looked in: " + dir.getCanonicalPath());
                  } catch (IOException var22) {
                     logger.info("WorldEdit found no snapshots: looked in (NON-RESOLVABLE PATH - does it exist?): " + dir.getPath());
                  }

                  return;
               }
            } catch (MissingWorldException var26) {
               player.printError("No snapshots were found for this world.");
               return;
            }
         }

         ChunkStore chunkStore = null;

         try {
            chunkStore = snapshot.getChunkStore();
            player.print("Snapshot '" + snapshot.getName() + "' loaded; now restoring...");
         } catch (DataException var24) {
            player.printError("Failed to load snapshot: " + var24.getMessage());
            return;
         } catch (IOException var25) {
            player.printError("Failed to load snapshot: " + var25.getMessage());
            return;
         }

         try {
            SnapshotRestore restore = new SnapshotRestore(chunkStore, editSession, region);
            restore.restore();
            if (restore.hadTotalFailure()) {
               String error = restore.getLastErrorMessage();
               if (error != null) {
                  player.printError("Errors prevented any blocks from being restored.");
                  player.printError("Last error: " + error);
               } else {
                  player.printError("No chunks could be loaded. (Bad archive?)");
               }
            } else {
               player.print(
                  String.format("Restored; %d missing chunks and %d other errors.", restore.getMissingChunks().size(), restore.getErrorChunks().size())
               );
            }
         } finally {
            try {
               chunkStore.close();
            } catch (IOException var21) {
            }
         }
      }
   }
}
