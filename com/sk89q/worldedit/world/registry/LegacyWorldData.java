package com.sk89q.worldedit.world.registry;

public class LegacyWorldData implements WorldData {
   private static final LegacyWorldData INSTANCE = new LegacyWorldData();
   private final LegacyBlockRegistry blockRegistry = new LegacyBlockRegistry();
   private final NullItemRegistry itemRegistry = new NullItemRegistry();
   private final NullEntityRegistry entityRegistry = new NullEntityRegistry();
   private final NullBiomeRegistry biomeRegistry = new NullBiomeRegistry();

   protected LegacyWorldData() {
   }

   @Override
   public BlockRegistry getBlockRegistry() {
      return this.blockRegistry;
   }

   @Override
   public ItemRegistry getItemRegistry() {
      return this.itemRegistry;
   }

   @Override
   public EntityRegistry getEntityRegistry() {
      return this.entityRegistry;
   }

   @Override
   public BiomeRegistry getBiomeRegistry() {
      return this.biomeRegistry;
   }

   public static LegacyWorldData getInstance() {
      return INSTANCE;
   }
}
