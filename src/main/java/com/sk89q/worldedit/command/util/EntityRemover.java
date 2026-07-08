package com.sk89q.worldedit.command.util;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.entity.metadata.EntityType;
import com.sk89q.worldedit.function.EntityFunction;
import com.sk89q.worldedit.world.registry.EntityRegistry;
import java.util.regex.Pattern;
import javax.annotation.Nullable;

public class EntityRemover {
   private EntityRemover.Type type;

   public void fromString(String str) throws CommandException {
      EntityRemover.Type type = EntityRemover.Type.findByPattern(str);
      if (type != null) {
         this.type = type;
      } else {
         throw new CommandException("Acceptable types: projectiles, items, paintings, itemframes, boats, minecarts, tnt, xp, or all");
      }
   }

   public EntityFunction createFunction(EntityRegistry entityRegistry) {
      final EntityRemover.Type type = this.type;
      Preconditions.checkNotNull("type can't be null", type);
      return new EntityFunction() {
         @Override
         public boolean apply(Entity entity) throws WorldEditException {
            EntityType registryType = entity.getFacet(EntityType.class);
            if (registryType != null && type.matches(registryType)) {
               entity.remove();
               return true;
            } else {
               return false;
            }
         }
      };
   }

   public static enum Type {
      ALL("all") {
         @Override
         boolean matches(EntityType type) {
            for (EntityRemover.Type value : values()) {
               if (value != this && value.matches(type)) {
                  return true;
               }
            }

            return false;
         }
      },
      PROJECTILES("projectiles?|arrows?") {
         @Override
         boolean matches(EntityType type) {
            return type.isProjectile();
         }
      },
      ITEMS("items?|drops?") {
         @Override
         boolean matches(EntityType type) {
            return type.isItem();
         }
      },
      FALLING_BLOCKS("falling(blocks?|sand|gravel)") {
         @Override
         boolean matches(EntityType type) {
            return type.isFallingBlock();
         }
      },
      PAINTINGS("paintings?|art") {
         @Override
         boolean matches(EntityType type) {
            return type.isPainting();
         }
      },
      ITEM_FRAMES("(item)frames?") {
         @Override
         boolean matches(EntityType type) {
            return type.isItemFrame();
         }
      },
      BOATS("boats?") {
         @Override
         boolean matches(EntityType type) {
            return type.isBoat();
         }
      },
      MINECARTS("(mine)?carts?") {
         @Override
         boolean matches(EntityType type) {
            return type.isMinecart();
         }
      },
      TNT("tnt") {
         @Override
         boolean matches(EntityType type) {
            return type.isTNT();
         }
      },
      XP_ORBS("xp") {
         @Override
         boolean matches(EntityType type) {
            return type.isExperienceOrb();
         }
      };

      private final Pattern pattern;

      private Type(String pattern) {
         this.pattern = Pattern.compile(pattern);
      }

      public boolean matches(String str) {
         return this.pattern.matcher(str).matches();
      }

      abstract boolean matches(EntityType var1);

      @Nullable
      public static EntityRemover.Type findByPattern(String str) {
         for (EntityRemover.Type type : values()) {
            if (type.matches(str)) {
               return type;
            }
         }

         return null;
      }
   }
}
