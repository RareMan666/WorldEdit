package com.sk89q.worldedit.entity.metadata;

public interface EntityType {
   boolean isPlayerDerived();

   boolean isProjectile();

   boolean isItem();

   boolean isFallingBlock();

   boolean isPainting();

   boolean isItemFrame();

   boolean isBoat();

   boolean isMinecart();

   boolean isTNT();

   boolean isExperienceOrb();

   boolean isLiving();

   boolean isAnimal();

   boolean isAmbient();

   boolean isNPC();

   boolean isGolem();

   boolean isTamed();

   boolean isTagged();

   boolean isArmorStand();
}
