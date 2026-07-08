package com.sk89q.worldedit.function.generator;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;
import java.util.Random;

public class GardenPatchGenerator implements RegionFunction {
   private final Random random = new Random();
   private final EditSession editSession;
   private Pattern plant = getPumpkinPattern();
   private int affected;

   public GardenPatchGenerator(EditSession editSession) {
      this.editSession = editSession;
   }

   public Pattern getPlant() {
      return this.plant;
   }

   public void setPlant(Pattern plant) {
      this.plant = plant;
   }

   public int getAffected() {
      return this.affected;
   }

   private void placeVine(Vector basePos, Vector pos) throws MaxChangedBlocksException {
      if (!(pos.distance(basePos) > 4.0)) {
         if (this.editSession.getBlockType(pos) == 0) {
            for (int i = -1; i > -3; i--) {
               Vector testPos = pos.add(0, i, 0);
               if (this.editSession.getBlockType(testPos) != 0) {
                  break;
               }

               pos = testPos;
            }

            this.editSession.setBlockIfAir(pos, new BaseBlock(18));
            this.affected++;
            int t = this.random.nextInt(4);
            int h = this.random.nextInt(3) - 1;
            BaseBlock log = new BaseBlock(17);
            switch (t) {
               case 0: {
                  if (this.random.nextBoolean()) {
                     this.placeVine(basePos, pos.add(1, 0, 0));
                  }

                  if (this.random.nextBoolean()) {
                     this.editSession.setBlockIfAir(pos.add(1, h, -1), log);
                     this.affected++;
                  }

                  Vector p;
                  this.editSession.setBlockIfAir(p = pos.add(0, 0, -1), this.plant.apply(p));
                  this.affected++;
                  break;
               }
               case 1: {
                  if (this.random.nextBoolean()) {
                     this.placeVine(basePos, pos.add(0, 0, 1));
                  }

                  if (this.random.nextBoolean()) {
                     this.editSession.setBlockIfAir(pos.add(1, h, 0), log);
                     this.affected++;
                  }

                  Vector p;
                  this.editSession.setBlockIfAir(p = pos.add(1, 0, 1), this.plant.apply(p));
                  this.affected++;
                  break;
               }
               case 2: {
                  if (this.random.nextBoolean()) {
                     this.placeVine(basePos, pos.add(0, 0, -1));
                  }

                  if (this.random.nextBoolean()) {
                     this.editSession.setBlockIfAir(pos.add(-1, h, 0), log);
                     this.affected++;
                  }

                  Vector p;
                  this.editSession.setBlockIfAir(p = pos.add(-1, 0, 1), this.plant.apply(p));
                  this.affected++;
                  break;
               }
               case 3: {
                  if (this.random.nextBoolean()) {
                     this.placeVine(basePos, pos.add(-1, 0, 0));
                  }

                  if (this.random.nextBoolean()) {
                     this.editSession.setBlockIfAir(pos.add(-1, h, -1), log);
                     this.affected++;
                  }

                  Vector p;
                  this.editSession.setBlockIfAir(p = pos.add(-1, 0, -1), this.plant.apply(p));
                  this.affected++;
               }
            }
         }
      }
   }

   @Override
   public boolean apply(Vector position) throws WorldEditException {
      if (this.editSession.getBlock(position).getType() != 0) {
         position = position.add(0, 1, 0);
      }

      if (this.editSession.getBlock(position.add(0, -1, 0)).getType() != 2) {
         return false;
      } else {
         BaseBlock leavesBlock = new BaseBlock(18);
         this.editSession.setBlockIfAir(position, leavesBlock);
         this.placeVine(position, position.add(0, 0, 1));
         this.placeVine(position, position.add(0, 0, -1));
         this.placeVine(position, position.add(1, 0, 0));
         this.placeVine(position, position.add(-1, 0, 0));
         return true;
      }
   }

   public static Pattern getPumpkinPattern() {
      RandomPattern pattern = new RandomPattern();

      for (int i = 0; i < 4; i++) {
         pattern.add(new BlockPattern(new BaseBlock(86, i)), 100.0);
      }

      return pattern;
   }

   public static Pattern getMelonPattern() {
      return new BlockPattern(new BaseBlock(103));
   }
}
