package com.sk89q.worldedit.bukkit;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.blocks.LazyBlock;
import com.sk89q.worldedit.bukkit.adapter.BukkitImplAdapter;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.registry.WorldData;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class BukkitWorld extends LocalWorld {
   private static final Logger logger = WorldEdit.logger;
   private static final Map<Integer, Effect> effects = new HashMap<>();
   private final WeakReference<World> worldRef;
   private static final EnumMap<TreeGenerator.TreeType, TreeType> treeTypeMapping;

   public BukkitWorld(World world) {
      this.worldRef = new WeakReference<>(world);
   }

   @Override
   public List<com.sk89q.worldedit.entity.Entity> getEntities(Region region) {
      World world = this.getWorld();
      List<Entity> ents = world.getEntities();
      List<com.sk89q.worldedit.entity.Entity> entities = new ArrayList<>();

      for (Entity ent : ents) {
         if (region.contains(BukkitUtil.toVector(ent.getLocation()))) {
            entities.add(BukkitAdapter.adapt(ent));
         }
      }

      return entities;
   }

   @Override
   public List<com.sk89q.worldedit.entity.Entity> getEntities() {
      List<com.sk89q.worldedit.entity.Entity> list = new ArrayList<>();

      for (Entity entity : this.getWorld().getEntities()) {
         list.add(BukkitAdapter.adapt(entity));
      }

      return list;
   }

   @Nullable
   @Override
   public com.sk89q.worldedit.entity.Entity createEntity(Location location, BaseEntity entity) {
      BukkitImplAdapter adapter = WorldEditPlugin.getInstance().getBukkitImplAdapter();
      if (adapter != null) {
         Entity createdEntity = adapter.createEntity(BukkitAdapter.adapt(this.getWorld(), location), entity);
         return createdEntity != null ? new BukkitEntity(createdEntity) : null;
      } else {
         return null;
      }
   }

   public World getWorld() {
      return (World)Preconditions.checkNotNull(this.worldRef.get(), "The world was unloaded and the reference is unavailable");
   }

   protected World getWorldChecked() throws WorldEditException {
      World world = this.worldRef.get();
      if (world == null) {
         throw new WorldUnloadedException();
      } else {
         return world;
      }
   }

   @Override
   public String getName() {
      return this.getWorld().getName();
   }

   @Override
   public int getBlockLightLevel(Vector pt) {
      return this.getWorld().getBlockAt(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ()).getLightLevel();
   }

   @Override
   public boolean regenerate(Region region, EditSession editSession) {
      BaseBlock[] history = new BaseBlock[256 * (this.getMaxY() + 1)];

      for (Vector2D chunk : region.getChunks()) {
         Vector min = new Vector(chunk.getBlockX() * 16, 0, chunk.getBlockZ() * 16);

         for (int x = 0; x < 16; x++) {
            for (int y = 0; y < this.getMaxY() + 1; y++) {
               for (int z = 0; z < 16; z++) {
                  Vector pt = min.add(x, y, z);
                  int index = y * 16 * 16 + z * 16 + x;
                  history[index] = editSession.getBlock(pt);
               }
            }
         }

         try {
            this.getWorld().regenerateChunk(chunk.getBlockX(), chunk.getBlockZ());
         } catch (Throwable var12) {
            logger.log(Level.WARNING, "Chunk generation via Bukkit raised an error", var12);
         }

         for (int x = 0; x < 16; x++) {
            for (int y = 0; y < this.getMaxY() + 1; y++) {
               for (int z = 0; z < 16; z++) {
                  Vector pt = min.add(x, y, z);
                  int index = y * 16 * 16 + z * 16 + x;
                  if (!region.contains(pt)) {
                     editSession.smartSetBlock(pt, history[index]);
                  } else {
                     editSession.rememberChange(pt, history[index], editSession.rawGetBlock(pt));
                  }
               }
            }
         }
      }

      return true;
   }

   private Inventory getBlockInventory(Chest chest) {
      try {
         return chest.getBlockInventory();
      } catch (Throwable var4) {
         if (chest.getInventory() instanceof DoubleChestInventory) {
            DoubleChestInventory inven = (DoubleChestInventory)chest.getInventory();
            if (inven.getLeftSide().getHolder().equals(chest)) {
               return inven.getLeftSide();
            } else {
               return (Inventory)(inven.getRightSide().getHolder().equals(chest) ? inven.getRightSide() : inven);
            }
         } else {
            return chest.getInventory();
         }
      }
   }

   @Override
   public boolean clearContainerBlockContents(Vector pt) {
      Block block = this.getWorld().getBlockAt(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ());
      if (block == null) {
         return false;
      } else {
         BlockState state = block.getState();
         if (!(state instanceof InventoryHolder)) {
            return false;
         } else {
            InventoryHolder chest = (InventoryHolder)state;
            Inventory inven = chest.getInventory();
            if (chest instanceof Chest) {
               inven = this.getBlockInventory((Chest)chest);
            }

            inven.clear();
            return true;
         }
      }
   }

   @Deprecated
   @Override
   public boolean generateTree(EditSession editSession, Vector pt) {
      return this.generateTree(TreeGenerator.TreeType.TREE, editSession, pt);
   }

   @Deprecated
   @Override
   public boolean generateBigTree(EditSession editSession, Vector pt) {
      return this.generateTree(TreeGenerator.TreeType.BIG_TREE, editSession, pt);
   }

   @Deprecated
   @Override
   public boolean generateBirchTree(EditSession editSession, Vector pt) {
      return this.generateTree(TreeGenerator.TreeType.BIRCH, editSession, pt);
   }

   @Deprecated
   @Override
   public boolean generateRedwoodTree(EditSession editSession, Vector pt) {
      return this.generateTree(TreeGenerator.TreeType.REDWOOD, editSession, pt);
   }

   @Deprecated
   @Override
   public boolean generateTallRedwoodTree(EditSession editSession, Vector pt) {
      return this.generateTree(TreeGenerator.TreeType.TALL_REDWOOD, editSession, pt);
   }

   public static TreeType toBukkitTreeType(TreeGenerator.TreeType type) {
      return treeTypeMapping.get(type);
   }

   @Override
   public boolean generateTree(TreeGenerator.TreeType type, EditSession editSession, Vector pt) {
      World world = this.getWorld();
      TreeType bukkitType = toBukkitTreeType(type);
      return type != null && world.generateTree(BukkitUtil.toLocation(world, pt), bukkitType, new EditSessionBlockChangeDelegate(editSession));
   }

   @Override
   public void dropItem(Vector pt, BaseItemStack item) {
      World world = this.getWorld();
      ItemStack bukkitItem = new ItemStack(item.getType(), item.getAmount(), item.getData());
      world.dropItemNaturally(BukkitUtil.toLocation(world, pt), bukkitItem);
   }

   @Override
   public boolean isValidBlockType(int type) {
      return Material.getMaterial(type) != null && Material.getMaterial(type).isBlock();
   }

   @Override
   public void checkLoadedChunk(Vector pt) {
      World world = this.getWorld();
      if (!world.isChunkLoaded(pt.getBlockX() >> 4, pt.getBlockZ() >> 4)) {
         world.loadChunk(pt.getBlockX() >> 4, pt.getBlockZ() >> 4);
      }
   }

   @Override
   public boolean equals(Object other) {
      if (other == null) {
         return false;
      } else if (other instanceof BukkitWorld) {
         return ((BukkitWorld)other).getWorld().equals(this.getWorld());
      } else {
         return other instanceof com.sk89q.worldedit.world.World ? ((com.sk89q.worldedit.world.World)other).getName().equals(this.getName()) : false;
      }
   }

   @Override
   public int hashCode() {
      return this.getWorld().hashCode();
   }

   @Override
   public int getMaxY() {
      return this.getWorld().getMaxHeight() - 1;
   }

   @Override
   public void fixAfterFastMode(Iterable<BlockVector2D> chunks) {
      World world = this.getWorld();

      for (BlockVector2D chunkPos : chunks) {
         world.refreshChunk(chunkPos.getBlockX(), chunkPos.getBlockZ());
      }
   }

   @Override
   public boolean playEffect(Vector position, int type, int data) {
      World world = this.getWorld();
      Effect effect = effects.get(type);
      if (effect == null) {
         return false;
      } else {
         world.playEffect(BukkitUtil.toLocation(world, position), effect, data);
         return true;
      }
   }

   @Override
   public WorldData getWorldData() {
      return BukkitWorldData.getInstance();
   }

   @Override
   public void simulateBlockMine(Vector pt) {
      this.getWorld().getBlockAt(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ()).breakNaturally();
   }

   @Override
   public BaseBlock getBlock(Vector position) {
      BukkitImplAdapter adapter = WorldEditPlugin.getInstance().getBukkitImplAdapter();
      if (adapter != null) {
         return adapter.getBlock(BukkitAdapter.adapt(this.getWorld(), position));
      } else {
         Block bukkitBlock = this.getWorld().getBlockAt(position.getBlockX(), position.getBlockY(), position.getBlockZ());
         return new BaseBlock(bukkitBlock.getTypeId(), bukkitBlock.getData());
      }
   }

   @Override
   public boolean setBlock(Vector position, BaseBlock block, boolean notifyAndLight) throws WorldEditException {
      BukkitImplAdapter adapter = WorldEditPlugin.getInstance().getBukkitImplAdapter();
      if (adapter != null) {
         return adapter.setBlock(BukkitAdapter.adapt(this.getWorld(), position), block, notifyAndLight);
      } else {
         Block bukkitBlock = this.getWorld().getBlockAt(position.getBlockX(), position.getBlockY(), position.getBlockZ());
         return bukkitBlock.setTypeIdAndData(block.getType(), (byte)block.getData(), notifyAndLight);
      }
   }

   @Override
   public BaseBlock getLazyBlock(Vector position) {
      World world = this.getWorld();
      Block bukkitBlock = world.getBlockAt(position.getBlockX(), position.getBlockY(), position.getBlockZ());
      return new LazyBlock(bukkitBlock.getTypeId(), bukkitBlock.getData(), this, position);
   }

   @Override
   public BaseBiome getBiome(Vector2D position) {
      BukkitImplAdapter adapter = WorldEditPlugin.getInstance().getBukkitImplAdapter();
      if (adapter != null) {
         int id = adapter.getBiomeId(this.getWorld().getBiome(position.getBlockX(), position.getBlockZ()));
         return new BaseBiome(id);
      } else {
         return new BaseBiome(0);
      }
   }

   @Override
   public boolean setBiome(Vector2D position, BaseBiome biome) {
      BukkitImplAdapter adapter = WorldEditPlugin.getInstance().getBukkitImplAdapter();
      if (adapter != null) {
         Biome bukkitBiome = adapter.getBiome(biome.getId());
         this.getWorld().setBiome(position.getBlockX(), position.getBlockZ(), bukkitBiome);
         return true;
      } else {
         return false;
      }
   }

   @Deprecated
   public boolean setBlock(Vector pt, com.sk89q.worldedit.foundation.Block block, boolean notifyAdjacent) throws WorldEditException {
      return this.setBlock(pt, (BaseBlock)block, notifyAdjacent);
   }

   static {
      for (Effect effect : Effect.values()) {
         effects.put(effect.getId(), effect);
      }

      treeTypeMapping = new EnumMap<>(TreeGenerator.TreeType.class);

      for (TreeGenerator.TreeType type : TreeGenerator.TreeType.values()) {
         try {
            TreeType bukkitType = TreeType.valueOf(type.name());
            treeTypeMapping.put(type, bukkitType);
         } catch (IllegalArgumentException var5) {
         }
      }

      treeTypeMapping.put(TreeGenerator.TreeType.SHORT_JUNGLE, TreeType.SMALL_JUNGLE);
      treeTypeMapping.put(TreeGenerator.TreeType.RANDOM, TreeType.BROWN_MUSHROOM);
      treeTypeMapping.put(TreeGenerator.TreeType.RANDOM_REDWOOD, TreeType.REDWOOD);
      treeTypeMapping.put(TreeGenerator.TreeType.PINE, TreeType.REDWOOD);
      treeTypeMapping.put(TreeGenerator.TreeType.RANDOM_BIRCH, TreeType.BIRCH);
      treeTypeMapping.put(TreeGenerator.TreeType.RANDOM_JUNGLE, TreeType.JUNGLE);
      treeTypeMapping.put(TreeGenerator.TreeType.RANDOM_MUSHROOM, TreeType.BROWN_MUSHROOM);

      for (TreeGenerator.TreeType type : TreeGenerator.TreeType.values()) {
         if (treeTypeMapping.get(type) == null) {
            WorldEdit.logger.severe("No TreeType mapping for TreeGenerator.TreeType." + type);
         }
      }
   }
}
