package com.sk89q.worldedit.bukkit;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.BlockWorldVector;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.NotABlockException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.blocks.SkullBlock;
import com.sk89q.worldedit.bukkit.entity.BukkitExpOrb;
import com.sk89q.worldedit.bukkit.entity.BukkitItem;
import com.sk89q.worldedit.bukkit.entity.BukkitPainting;
import java.util.List;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

public final class BukkitUtil {
   public static final double EQUALS_PRECISION = 1.0E-4;

   private BukkitUtil() {
   }

   public static LocalWorld getLocalWorld(World w) {
      return new BukkitWorld(w);
   }

   public static BlockVector toVector(Block block) {
      return new BlockVector(block.getX(), block.getY(), block.getZ());
   }

   public static BlockVector toVector(BlockFace face) {
      return new BlockVector(face.getModX(), face.getModY(), face.getModZ());
   }

   public static BlockWorldVector toWorldVector(Block block) {
      return new BlockWorldVector(getLocalWorld(block.getWorld()), block.getX(), block.getY(), block.getZ());
   }

   public static Vector toVector(Location loc) {
      return new Vector(loc.getX(), loc.getY(), loc.getZ());
   }

   public static com.sk89q.worldedit.Location toLocation(Location loc) {
      return new com.sk89q.worldedit.Location(getLocalWorld(loc.getWorld()), new Vector(loc.getX(), loc.getY(), loc.getZ()), loc.getYaw(), loc.getPitch());
   }

   public static Vector toVector(org.bukkit.util.Vector vector) {
      return new Vector(vector.getX(), vector.getY(), vector.getZ());
   }

   public static Location toLocation(WorldVector pt) {
      return new Location(toWorld(pt), pt.getX(), pt.getY(), pt.getZ());
   }

   public static Location toLocation(World world, Vector pt) {
      return new Location(world, pt.getX(), pt.getY(), pt.getZ());
   }

   public static Location center(Location loc) {
      return new Location(loc.getWorld(), loc.getBlockX() + 0.5, loc.getBlockY() + 0.5, loc.getBlockZ() + 0.5, loc.getPitch(), loc.getYaw());
   }

   public static Player matchSinglePlayer(Server server, String name) {
      List<Player> players = server.matchPlayer(name);
      return players.isEmpty() ? null : players.get(0);
   }

   public static Block toBlock(BlockWorldVector pt) {
      return toWorld(pt).getBlockAt(toLocation(pt));
   }

   public static World toWorld(WorldVector pt) {
      return ((BukkitWorld)pt.getWorld()).getWorld();
   }

   public static boolean equals(Location a, Location b) {
      if (Math.abs(a.getX() - b.getX()) > 1.0E-4) {
         return false;
      } else {
         return Math.abs(a.getY() - b.getY()) > 1.0E-4 ? false : !(Math.abs(a.getZ() - b.getZ()) > 1.0E-4);
      }
   }

   public static Location toLocation(com.sk89q.worldedit.Location location) {
      Vector pt = location.getPosition();
      return new Location(toWorld(location.getWorld()), pt.getX(), pt.getY(), pt.getZ(), location.getYaw(), location.getPitch());
   }

   public static World toWorld(LocalWorld world) {
      return ((BukkitWorld)world).getWorld();
   }

   public static com.sk89q.worldedit.bukkit.entity.BukkitEntity toLocalEntity(Entity e) {
      switch (e.getType()) {
         case EXPERIENCE_ORB:
            return new BukkitExpOrb(toLocation(e.getLocation()), e.getUniqueId(), ((ExperienceOrb)e).getExperience());
         case PAINTING:
            Painting paint = (Painting)e;
            return new BukkitPainting(toLocation(e.getLocation()), paint.getArt(), paint.getFacing(), e.getUniqueId());
         case DROPPED_ITEM:
            return new BukkitItem(toLocation(e.getLocation()), ((Item)e).getItemStack(), e.getUniqueId());
         default:
            return new com.sk89q.worldedit.bukkit.entity.BukkitEntity(toLocation(e.getLocation()), e.getType(), e.getUniqueId());
      }
   }

   public static BaseBlock toBlock(LocalWorld world, ItemStack itemStack) throws WorldEditException {
      int typeId = itemStack.getTypeId();
      switch (typeId) {
         case 351:
            Dye materialData = (Dye)itemStack.getData();
            if (materialData.getColor() == DyeColor.BROWN) {
               return new BaseBlock(127, -1);
            }
            break;
         case 397:
            return new SkullBlock(0, (byte)itemStack.getDurability());
         default:
            BaseBlock baseBlock = BlockType.getBlockForItem(typeId, itemStack.getDurability());
            if (baseBlock != null) {
               return baseBlock;
            }
      }

      if (world.isValidBlockType(typeId)) {
         return new BaseBlock(typeId, -1);
      } else {
         throw new NotABlockException(typeId);
      }
   }
}
