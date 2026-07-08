package com.sk89q.worldedit.internal;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.entity.BaseEntity;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.registry.WorldData;
import java.util.List;
import javax.annotation.Nullable;

public class LocalWorldAdapter extends LocalWorld {
   private final World world;

   private LocalWorldAdapter(World world) {
      Preconditions.checkNotNull(world);
      this.world = world;
   }

   @Override
   public String getName() {
      return this.world.getName();
   }

   @Override
   public int getMaxY() {
      return this.world.getMaxY();
   }

   @Override
   public boolean isValidBlockType(int id) {
      return this.world.isValidBlockType(id);
   }

   @Override
   public boolean usesBlockData(int id) {
      return this.world.usesBlockData(id);
   }

   @Override
   public Mask createLiquidMask() {
      return this.world.createLiquidMask();
   }

   @Deprecated
   @Override
   public int getBlockType(Vector pt) {
      return this.world.getBlockType(pt);
   }

   @Deprecated
   @Override
   public int getBlockData(Vector pt) {
      return this.world.getBlockData(pt);
   }

   @Override
   public boolean setBlock(Vector position, BaseBlock block, boolean notifyAndLight) throws WorldEditException {
      return this.world.setBlock(position, block, notifyAndLight);
   }

   @Override
   public int getBlockLightLevel(Vector position) {
      return this.world.getBlockLightLevel(position);
   }

   @Override
   public boolean clearContainerBlockContents(Vector position) {
      return this.world.clearContainerBlockContents(position);
   }

   @Override
   public BaseBiome getBiome(Vector2D position) {
      return this.world.getBiome(position);
   }

   @Override
   public boolean setBiome(Vector2D position, BaseBiome biome) {
      return this.world.setBiome(position, biome);
   }

   @Override
   public void dropItem(Vector position, BaseItemStack item, int count) {
      this.world.dropItem(position, item, count);
   }

   @Override
   public void dropItem(Vector position, BaseItemStack item) {
      this.world.dropItem(position, item);
   }

   @Override
   public void simulateBlockMine(Vector position) {
      this.world.simulateBlockMine(position);
   }

   @Override
   public boolean regenerate(Region region, EditSession editSession) {
      return this.world.regenerate(region, editSession);
   }

   @Override
   public boolean generateTree(TreeGenerator.TreeType type, EditSession editSession, Vector position) throws MaxChangedBlocksException {
      return this.world.generateTree(type, editSession, position);
   }

   @Deprecated
   @Override
   public boolean generateTree(EditSession editSession, Vector position) throws MaxChangedBlocksException {
      return this.world.generateTree(editSession, position);
   }

   @Deprecated
   @Override
   public boolean generateBigTree(EditSession editSession, Vector position) throws MaxChangedBlocksException {
      return this.world.generateBigTree(editSession, position);
   }

   @Deprecated
   @Override
   public boolean generateBirchTree(EditSession editSession, Vector position) throws MaxChangedBlocksException {
      return this.world.generateBirchTree(editSession, position);
   }

   @Deprecated
   @Override
   public boolean generateRedwoodTree(EditSession editSession, Vector position) throws MaxChangedBlocksException {
      return this.world.generateRedwoodTree(editSession, position);
   }

   @Deprecated
   @Override
   public boolean generateTallRedwoodTree(EditSession editSession, Vector position) throws MaxChangedBlocksException {
      return this.world.generateTallRedwoodTree(editSession, position);
   }

   @Override
   public void checkLoadedChunk(Vector position) {
      this.world.checkLoadedChunk(position);
   }

   @Override
   public void fixAfterFastMode(Iterable<BlockVector2D> chunks) {
      this.world.fixAfterFastMode(chunks);
   }

   @Override
   public void fixLighting(Iterable<BlockVector2D> chunks) {
      this.world.fixLighting(chunks);
   }

   @Override
   public boolean playEffect(Vector position, int type, int data) {
      return this.world.playEffect(position, type, data);
   }

   @Override
   public boolean queueBlockBreakEffect(Platform server, Vector position, int blockId, double priority) {
      return this.world.queueBlockBreakEffect(server, position, blockId, priority);
   }

   @Override
   public WorldData getWorldData() {
      return this.world.getWorldData();
   }

   @Override
   public boolean equals(Object other) {
      return this.world.equals(other);
   }

   @Override
   public int hashCode() {
      return this.world.hashCode();
   }

   @Override
   public Vector getMinimumPoint() {
      return this.world.getMinimumPoint();
   }

   @Override
   public Vector getMaximumPoint() {
      return this.world.getMaximumPoint();
   }

   @Override
   public List<? extends Entity> getEntities(Region region) {
      return this.world.getEntities(region);
   }

   @Override
   public BaseBlock getBlock(Vector position) {
      return this.world.getBlock(position);
   }

   @Override
   public BaseBlock getLazyBlock(Vector position) {
      return this.world.getLazyBlock(position);
   }

   @Nullable
   @Override
   public Operation commit() {
      return this.world.commit();
   }

   @Nullable
   @Override
   public Entity createEntity(Location location, BaseEntity entity) {
      return this.world.createEntity(location, entity);
   }

   @Override
   public List<? extends Entity> getEntities() {
      return this.world.getEntities();
   }

   public static LocalWorldAdapter adapt(World world) {
      return new LocalWorldAdapter(world);
   }
}
