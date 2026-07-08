package com.sk89q.worldedit;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.event.extent.EditSessionEvent;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.util.eventbus.EventBus;
import com.sk89q.worldedit.world.World;

public class EditSessionFactory {
   public EditSession getEditSession(World world, int maxBlocks) {
      throw new RuntimeException("Method needs to be implemented");
   }

   public EditSession getEditSession(World world, int maxBlocks, Player player) {
      throw new RuntimeException("Method needs to be implemented");
   }

   public EditSession getEditSession(World world, int maxBlocks, BlockBag blockBag) {
      throw new RuntimeException("Method needs to be implemented");
   }

   public EditSession getEditSession(World world, int maxBlocks, BlockBag blockBag, Player player) {
      throw new RuntimeException("Method needs to be implemented");
   }

   @Deprecated
   public EditSession getEditSession(LocalWorld world, int maxBlocks) {
      return this.getEditSession((World)world, maxBlocks);
   }

   @Deprecated
   public EditSession getEditSession(LocalWorld world, int maxBlocks, LocalPlayer player) {
      return this.getEditSession((World)world, maxBlocks, (Player)player);
   }

   @Deprecated
   public EditSession getEditSession(LocalWorld world, int maxBlocks, BlockBag blockBag) {
      return this.getEditSession((World)world, maxBlocks, blockBag);
   }

   @Deprecated
   public EditSession getEditSession(LocalWorld world, int maxBlocks, BlockBag blockBag, LocalPlayer player) {
      return this.getEditSession((World)world, maxBlocks, blockBag, (Player)player);
   }

   static final class EditSessionFactoryImpl extends EditSessionFactory {
      private final EventBus eventBus;

      EditSessionFactoryImpl(EventBus eventBus) {
         Preconditions.checkNotNull(eventBus);
         this.eventBus = eventBus;
      }

      @Override
      public EditSession getEditSession(World world, int maxBlocks) {
         return new EditSession(this.eventBus, world, maxBlocks, null, new EditSessionEvent(world, null, maxBlocks, null));
      }

      @Override
      public EditSession getEditSession(World world, int maxBlocks, Player player) {
         return new EditSession(this.eventBus, world, maxBlocks, null, new EditSessionEvent(world, player, maxBlocks, null));
      }

      @Override
      public EditSession getEditSession(World world, int maxBlocks, BlockBag blockBag) {
         return new EditSession(this.eventBus, world, maxBlocks, blockBag, new EditSessionEvent(world, null, maxBlocks, null));
      }

      @Override
      public EditSession getEditSession(World world, int maxBlocks, BlockBag blockBag, Player player) {
         return new EditSession(this.eventBus, world, maxBlocks, blockBag, new EditSessionEvent(world, player, maxBlocks, null));
      }
   }
}
