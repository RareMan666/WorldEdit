package com.sk89q.worldedit.blocks;

public interface BlockMaterial {
   boolean isRenderedAsNormalBlock();

   boolean isFullCube();

   boolean isOpaque();

   boolean isPowerSource();

   boolean isLiquid();

   boolean isSolid();

   float getHardness();

   float getResistance();

   float getSlipperiness();

   boolean isGrassBlocking();

   float getAmbientOcclusionLightValue();

   int getLightOpacity();

   int getLightValue();

   boolean isFragileWhenPushed();

   boolean isUnpushable();

   boolean isAdventureModeExempt();

   boolean isTicksRandomly();

   boolean isUsingNeighborLight();

   boolean isMovementBlocker();

   boolean isBurnable();

   boolean isToolRequired();

   boolean isReplacedDuringPlacement();
}
