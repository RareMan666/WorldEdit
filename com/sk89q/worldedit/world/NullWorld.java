package com.sk89q.worldedit.world;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.registry.LegacyWorldData;
import com.sk89q.worldedit.world.registry.WorldData;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public class NullWorld extends AbstractWorld {
   private static final NullWorld INSTANCE = new NullWorld();

   protected NullWorld() {
   }

   @Override
   public String getName() {
      return "null";
   }

   @Override
   public boolean setBlock(Vector position, BaseBlock block, boolean notifyAndLight) throws WorldEditException {
      return false;
   }

   @Override
   public int getBlockLightLevel(Vector position) {
      return 0;
   }

   @Override
   public boolean clearContainerBlockContents(Vector position) {
      return false;
   }

   @Override
   public BaseBiome getBiome(Vector2D position) {
      return null;
   }

   @Override
   public boolean setBiome(Vector2D position, BaseBiome biome) {
      return false;
   }

   @Override
   public void dropItem(Vector position, BaseItemStack item) {
   }

   @Override
   public boolean regenerate(Region region, EditSession editSession) {
      return false;
   }

   @Override
   public boolean generateTree(TreeGenerator.TreeType type, EditSession editSession, Vector position) throws MaxChangedBlocksException {
      return false;
   }

   @Override
   public WorldData getWorldData() {
      return LegacyWorldData.getInstance();
   }

   @Override
   public BaseBlock getBlock(Vector position) {
      return new BaseBlock(0);
   }

   @Override
   public BaseBlock getLazyBlock(Vector position) {
      return new BaseBlock(0);
   }

   @Override
   public List<Entity> getEntities(Region region) {
      return Collections.emptyList();
   }

   @Override
   public List<Entity> getEntities() {
      return Collections.emptyList();
   }

   @Nullable
   @Override
   public Entity createEntity(Location location, BaseEntity entity) {
      return null;
   }

   public static NullWorld getInstance() {
      return INSTANCE;
   }
}
