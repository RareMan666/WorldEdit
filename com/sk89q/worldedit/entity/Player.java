package com.sk89q.worldedit.entity;

import com.sk89q.worldedit.PlayerDirection;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.WorldVectorFace;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.world.World;

public interface Player extends Entity, Actor {
   World getWorld();

   boolean isHoldingPickAxe();

   PlayerDirection getCardinalDirection(int var1);

   int getItemInHand();

   BaseBlock getBlockInHand() throws WorldEditException;

   void giveItem(int var1, int var2);

   BlockBag getInventoryBlockBag();

   boolean hasCreativeMode();

   void findFreePosition(WorldVector var1);

   void setOnGround(WorldVector var1);

   void findFreePosition();

   boolean ascendLevel();

   boolean descendLevel();

   boolean ascendToCeiling(int var1);

   boolean ascendToCeiling(int var1, boolean var2);

   boolean ascendUpwards(int var1);

   boolean ascendUpwards(int var1, boolean var2);

   void floatAt(int var1, int var2, int var3, boolean var4);

   WorldVector getBlockIn();

   WorldVector getBlockOn();

   WorldVector getBlockTrace(int var1, boolean var2);

   WorldVectorFace getBlockTraceFace(int var1, boolean var2);

   WorldVector getBlockTrace(int var1);

   WorldVector getSolidBlockTrace(int var1);

   PlayerDirection getCardinalDirection();

   @Deprecated
   WorldVector getPosition();

   @Deprecated
   double getPitch();

   @Deprecated
   double getYaw();

   boolean passThroughForwardWall(int var1);

   void setPosition(Vector var1, float var2, float var3);

   void setPosition(Vector var1);
}
