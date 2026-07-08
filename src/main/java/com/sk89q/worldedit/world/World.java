package com.sk89q.worldedit.world;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BaseItem;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.extension.platform.Platform;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.world.registry.WorldData;

public interface World extends Extent {
   String getName();

   int getMaxY();

   boolean isValidBlockType(int var1);

   boolean usesBlockData(int var1);

   Mask createLiquidMask();

   boolean useItem(Vector var1, BaseItem var2, Direction var3);

   @Deprecated
   int getBlockType(Vector var1);

   @Deprecated
   int getBlockData(Vector var1);

   boolean setBlock(Vector var1, BaseBlock var2, boolean var3) throws WorldEditException;

   @Deprecated
   boolean setBlockType(Vector var1, int var2);

   @Deprecated
   void setBlockData(Vector var1, int var2);

   @Deprecated
   boolean setTypeIdAndData(Vector var1, int var2, int var3);

   int getBlockLightLevel(Vector var1);

   boolean clearContainerBlockContents(Vector var1);

   void dropItem(Vector var1, BaseItemStack var2, int var3);

   void dropItem(Vector var1, BaseItemStack var2);

   void simulateBlockMine(Vector var1);

   boolean regenerate(Region var1, EditSession var2);

   boolean generateTree(TreeGenerator.TreeType var1, EditSession var2, Vector var3) throws MaxChangedBlocksException;

   @Deprecated
   boolean generateTree(EditSession var1, Vector var2) throws MaxChangedBlocksException;

   @Deprecated
   boolean generateBigTree(EditSession var1, Vector var2) throws MaxChangedBlocksException;

   @Deprecated
   boolean generateBirchTree(EditSession var1, Vector var2) throws MaxChangedBlocksException;

   @Deprecated
   boolean generateRedwoodTree(EditSession var1, Vector var2) throws MaxChangedBlocksException;

   @Deprecated
   boolean generateTallRedwoodTree(EditSession var1, Vector var2) throws MaxChangedBlocksException;

   void checkLoadedChunk(Vector var1);

   void fixAfterFastMode(Iterable<BlockVector2D> var1);

   void fixLighting(Iterable<BlockVector2D> var1);

   boolean playEffect(Vector var1, int var2, int var3);

   boolean queueBlockBreakEffect(Platform var1, Vector var2, int var3, double var4);

   WorldData getWorldData();

   @Override
   boolean equals(Object var1);

   @Override
   int hashCode();
}
