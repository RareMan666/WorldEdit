package com.sk89q.worldedit.command.util;

import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.entity.metadata.EntityType;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.function.EntityFunction;
import com.sk89q.worldedit.world.registry.EntityRegistry;

public class CreatureButcher {
   private final Actor player;
   public int flags = 0;

   public CreatureButcher(Actor player) {
      this.player = player;
   }

   public void or(int flag, boolean on) {
      if (on) {
         this.flags |= flag;
      }
   }

   public void or(int flag, boolean on, String permission) {
      this.or(flag, on);
      if ((this.flags & flag) != 0 && !this.player.hasPermission(permission)) {
         this.flags &= ~flag;
      }
   }

   public void fromCommand(CommandContext args) {
      this.or(63, args.hasFlag('f'));
      this.or(1, args.hasFlag('p'), "worldedit.butcher.pets");
      this.or(2, args.hasFlag('n'), "worldedit.butcher.npcs");
      this.or(8, args.hasFlag('g'), "worldedit.butcher.golems");
      this.or(4, args.hasFlag('a'), "worldedit.butcher.animals");
      this.or(16, args.hasFlag('b'), "worldedit.butcher.ambient");
      this.or(32, args.hasFlag('t'), "worldedit.butcher.tagged");
      this.or(64, args.hasFlag('r'), "worldedit.butcher.armorstands");
      this.or(1048576, args.hasFlag('l'), "worldedit.butcher.lightning");
   }

   public EntityFunction createFunction(EntityRegistry entityRegistry) {
      return new EntityFunction() {
         @Override
         public boolean apply(Entity entity) throws WorldEditException {
            boolean killPets = (CreatureButcher.this.flags & 1) != 0;
            boolean killNPCs = (CreatureButcher.this.flags & 2) != 0;
            boolean killAnimals = (CreatureButcher.this.flags & 4) != 0;
            boolean killGolems = (CreatureButcher.this.flags & 8) != 0;
            boolean killAmbient = (CreatureButcher.this.flags & 16) != 0;
            boolean killTagged = (CreatureButcher.this.flags & 32) != 0;
            boolean killArmorStands = (CreatureButcher.this.flags & 64) != 0;
            EntityType type = entity.getFacet(EntityType.class);
            if (type == null) {
               return false;
            } else if (type.isPlayerDerived()) {
               return false;
            } else if (!type.isLiving()) {
               return false;
            } else if (!killAnimals && type.isAnimal()) {
               return false;
            } else if (!killPets && type.isTamed()) {
               return false;
            } else if (!killGolems && type.isGolem()) {
               return false;
            } else if (!killNPCs && type.isNPC()) {
               return false;
            } else if (!killAmbient && type.isAmbient()) {
               return false;
            } else if (!killTagged && type.isTagged()) {
               return false;
            } else if (!killArmorStands && type.isArmorStand()) {
               return false;
            } else {
               entity.remove();
               return true;
            }
         }
      };
   }

   final class Flags {
      public static final int PETS = 1;
      public static final int NPCS = 2;
      public static final int ANIMALS = 4;
      public static final int GOLEMS = 8;
      public static final int AMBIENT = 16;
      public static final int TAGGED = 32;
      public static final int FRIENDLY = 63;
      public static final int ARMOR_STAND = 64;
      public static final int WITH_LIGHTNING = 1048576;

      private Flags() {
      }
   }
}
