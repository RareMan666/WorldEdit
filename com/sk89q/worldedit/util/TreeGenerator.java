package com.sk89q.worldedit.util;

import com.google.common.collect.Sets;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;

public class TreeGenerator {
   private static final Random RANDOM = new Random();
   private TreeGenerator.TreeType type;

   @Deprecated
   public TreeGenerator(TreeGenerator.TreeType type) {
      this.type = type;
   }

   public boolean generate(EditSession editSession, Vector position) throws MaxChangedBlocksException {
      return this.type.generate(editSession, position);
   }

   private static void makePineTree(EditSession editSession, Vector basePosition) throws MaxChangedBlocksException {
      int trunkHeight = (int)Math.floor(Math.random() * 2.0) + 3;
      int height = (int)Math.floor(Math.random() * 5.0) + 8;
      BaseBlock logBlock = new BaseBlock(17);
      BaseBlock leavesBlock = new BaseBlock(18);

      for (int i = 0; i < trunkHeight; i++) {
         if (!editSession.setBlockIfAir(basePosition.add(0, i, 0), logBlock)) {
            return;
         }
      }

      basePosition = basePosition.add(0, trunkHeight, 0);

      for (int ix = 0; ix < height; ix++) {
         editSession.setBlockIfAir(basePosition.add(0, ix, 0), logBlock);
         double chance = ix != 0 && ix != height - 1 ? 1.0 : 0.6;
         editSession.setChanceBlockIfAir(basePosition.add(-1, ix, 0), leavesBlock, chance);
         editSession.setChanceBlockIfAir(basePosition.add(1, ix, 0), leavesBlock, chance);
         editSession.setChanceBlockIfAir(basePosition.add(0, ix, -1), leavesBlock, chance);
         editSession.setChanceBlockIfAir(basePosition.add(0, ix, 1), leavesBlock, chance);
         editSession.setChanceBlockIfAir(basePosition.add(1, ix, 1), leavesBlock, chance);
         editSession.setChanceBlockIfAir(basePosition.add(-1, ix, 1), leavesBlock, chance);
         editSession.setChanceBlockIfAir(basePosition.add(1, ix, -1), leavesBlock, chance);
         editSession.setChanceBlockIfAir(basePosition.add(-1, ix, -1), leavesBlock, chance);
         if (ix != 0 && ix != height - 1) {
            for (int j = -2; j <= 2; j++) {
               editSession.setChanceBlockIfAir(basePosition.add(-2, ix, j), leavesBlock, 0.6);
            }

            for (int j = -2; j <= 2; j++) {
               editSession.setChanceBlockIfAir(basePosition.add(2, ix, j), leavesBlock, 0.6);
            }

            for (int j = -2; j <= 2; j++) {
               editSession.setChanceBlockIfAir(basePosition.add(j, ix, -2), leavesBlock, 0.6);
            }

            for (int j = -2; j <= 2; j++) {
               editSession.setChanceBlockIfAir(basePosition.add(j, ix, 2), leavesBlock, 0.6);
            }
         }
      }

      editSession.setBlockIfAir(basePosition.add(0, height, 0), leavesBlock);
   }

   @Nullable
   public static TreeGenerator.TreeType lookup(String type) {
      return TreeGenerator.TreeType.lookup(type);
   }

   public static enum TreeType {
      TREE("Oak tree", "oak", "tree", "regular"),
      BIG_TREE("Large oak tree", "largeoak", "bigoak", "big", "bigtree"),
      REDWOOD("Spruce tree", "spruce", "redwood", "sequoia", "sequoioideae"),
      TALL_REDWOOD("Tall spruce tree", "tallspruce", "bigspruce", "tallredwood", "tallsequoia", "tallsequoioideae"),
      MEGA_REDWOOD("Large spruce tree", "largespruce", "megaredwood"),
      RANDOM_REDWOOD("Random spruce tree", "randspruce", "randredwood", "randomredwood", "anyredwood") {
         @Override
         public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
            TreeGenerator.TreeType[] choices = new TreeGenerator.TreeType[]{REDWOOD, TALL_REDWOOD, MEGA_REDWOOD};
            return choices[TreeGenerator.RANDOM.nextInt(choices.length)].generate(editSession, pos);
         }
      },
      BIRCH("Birch tree", "birch", "white", "whitebark"),
      TALL_BIRCH("Tall birch tree", "tallbirch"),
      RANDOM_BIRCH("Random birch tree", "randbirch", "randombirch") {
         @Override
         public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
            TreeGenerator.TreeType[] choices = new TreeGenerator.TreeType[]{BIRCH, TALL_BIRCH};
            return choices[TreeGenerator.RANDOM.nextInt(choices.length)].generate(editSession, pos);
         }
      },
      JUNGLE("Jungle tree", "jungle"),
      SMALL_JUNGLE("Small jungle tree", "shortjungle", "smalljungle"),
      SHORT_JUNGLE("Short jungle tree") {
         @Override
         public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
            return SMALL_JUNGLE.generate(editSession, pos);
         }
      },
      RANDOM_JUNGLE("Random jungle tree", "randjungle", "randomjungle") {
         @Override
         public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
            TreeGenerator.TreeType[] choices = new TreeGenerator.TreeType[]{JUNGLE, SMALL_JUNGLE};
            return choices[TreeGenerator.RANDOM.nextInt(choices.length)].generate(editSession, pos);
         }
      },
      JUNGLE_BUSH("Jungle bush", "junglebush", "jungleshrub"),
      RED_MUSHROOM("Red mushroom", "redmushroom", "redgiantmushroom"),
      BROWN_MUSHROOM("Brown mushroom", "brownmushroom", "browngiantmushroom"),
      RANDOM_MUSHROOM("Random mushroom", "randmushroom", "randommushroom") {
         @Override
         public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
            TreeGenerator.TreeType[] choices = new TreeGenerator.TreeType[]{RED_MUSHROOM, BROWN_MUSHROOM};
            return choices[TreeGenerator.RANDOM.nextInt(choices.length)].generate(editSession, pos);
         }
      },
      SWAMP("Swamp tree", "swamp", "swamptree"),
      ACACIA("Acacia tree", "acacia"),
      DARK_OAK("Dark oak tree", "darkoak"),
      PINE("Pine tree", "pine") {
         @Override
         public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
            TreeGenerator.makePineTree(editSession, pos);
            return true;
         }
      },
      RANDOM("Random tree", "rand", "random") {
         @Override
         public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
            TreeGenerator.TreeType[] choices = TreeGenerator.TreeType.values();
            return choices[TreeGenerator.RANDOM.nextInt(choices.length)].generate(editSession, pos);
         }
      };

      private static final Map<String, TreeGenerator.TreeType> lookup = new HashMap<>();
      private static final Set<String> primaryAliases = Sets.newHashSet();
      private final String name;
      private final String[] lookupKeys;

      private TreeType(String name, String... lookupKeys) {
         this.name = name;
         this.lookupKeys = lookupKeys;
      }

      public static Set<String> getAliases() {
         return Collections.unmodifiableSet(lookup.keySet());
      }

      public static Set<String> getPrimaryAliases() {
         return Collections.unmodifiableSet(primaryAliases);
      }

      public boolean generate(EditSession editSession, Vector pos) throws MaxChangedBlocksException {
         return editSession.getWorld().generateTree(this, editSession, pos);
      }

      public String getName() {
         return this.name;
      }

      @Nullable
      public static TreeGenerator.TreeType lookup(String name) {
         return lookup.get(name.toLowerCase());
      }

      static {
         for (TreeGenerator.TreeType type : EnumSet.allOf(TreeGenerator.TreeType.class)) {
            for (String key : type.lookupKeys) {
               lookup.put(key, type);
            }

            if (type.lookupKeys.length > 0) {
               primaryAliases.add(type.lookupKeys[0]);
            }
         }
      }
   }
}
