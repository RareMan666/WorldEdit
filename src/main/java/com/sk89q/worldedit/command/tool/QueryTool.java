package com.sk89q.worldedit.command.tool;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.blocks.ClothColor;
import com.sk89q.worldedit.blocks.MobSpawnerBlock;
import com.sk89q.worldedit.blocks.NoteBlock;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.BundledBlockData;
import com.sk89q.worldedit.world.registry.SimpleState;
import com.sk89q.worldedit.world.registry.State;
import com.sk89q.worldedit.world.registry.StateValue;
import java.util.Map;
import java.util.Map.Entry;

public class QueryTool implements BlockTool {
   @Override
   public boolean canUse(Actor player) {
      return player.hasPermission("worldedit.tool.info");
   }

   @Override
   public boolean actPrimary(Platform server, LocalConfiguration config, Player player, LocalSession session, Location clicked) {
      World world = (World)clicked.getExtent();
      EditSession editSession = session.createEditSession(player);
      BaseBlock block = editSession.rawGetBlock(clicked.toVector());
      BlockType type = BlockType.fromID(block.getType());
      player.print(
         "§9@"
            + clicked.toVector()
            + ": §e#"
            + block.getType()
            + "§7 ("
            + (type == null ? "Unknown" : type.getName())
            + ") §f["
            + block.getData()
            + "] ("
            + world.getBlockLightLevel(clicked.toVector())
            + "/"
            + world.getBlockLightLevel(clicked.toVector().add(0, 1, 0))
            + ")"
      );
      if (block instanceof MobSpawnerBlock) {
         player.printRaw("§eMob Type: " + ((MobSpawnerBlock)block).getMobType());
      } else if (block instanceof NoteBlock) {
         player.printRaw("§eNote block: " + ((NoteBlock)block).getNote());
      } else if (block.getType() == 35) {
         player.printRaw("§eColor: " + ClothColor.fromID(block.getData()).getName());
      }

      Map<String, ? extends State> states = BundledBlockData.getInstance().getStatesById(block.getId());
      if (states != null && !states.isEmpty()) {
         StringBuilder builder = new StringBuilder();
         builder.append("States: ");
         boolean first = true;
         boolean hasVisibleStates = false;

         for (Entry<String, ? extends State> e : states.entrySet()) {
            String name = e.getKey();
            State state = e.getValue();
            if (!(state instanceof SimpleState) || ((SimpleState)state).getDataMask() != 0) {
               hasVisibleStates = true;
               if (!first) {
                  builder.append(", ");
               }

               first = false;
               String valName = "";

               for (Entry<String, ? extends StateValue> entry : state.valueMap().entrySet()) {
                  if (entry.getValue().isSet(block)) {
                     valName = entry.getKey();
                     break;
                  }
               }

               builder.append("§9").append(name).append(": §f").append(valName != null ? valName : "set");
            }
         }

         if (hasVisibleStates) {
            player.printRaw(builder.toString());
         }

         return true;
      } else {
         return true;
      }
   }
}
