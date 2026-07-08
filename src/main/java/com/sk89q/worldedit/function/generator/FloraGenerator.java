package com.sk89q.worldedit.function.generator;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.RandomPattern;

public class FloraGenerator implements RegionFunction {
   private final EditSession editSession;
   private boolean biomeAware = false;
   private final Pattern desertPattern = getDesertPattern();
   private final Pattern temperatePattern = getTemperatePattern();

   public FloraGenerator(EditSession editSession) {
      this.editSession = editSession;
   }

   public boolean isBiomeAware() {
      return this.biomeAware;
   }

   public void setBiomeAware(boolean biomeAware) {
      if (biomeAware) {
         throw new IllegalArgumentException("Cannot enable biome-aware mode; not yet implemented");
      }
   }

   public static Pattern getDesertPattern() {
      RandomPattern pattern = new RandomPattern();
      pattern.add(new BlockPattern(new BaseBlock(32)), 30.0);
      pattern.add(new BlockPattern(new BaseBlock(81)), 20.0);
      pattern.add(new BlockPattern(new BaseBlock(0)), 300.0);
      return pattern;
   }

   public static Pattern getTemperatePattern() {
      RandomPattern pattern = new RandomPattern();
      pattern.add(new BlockPattern(new BaseBlock(31, 1)), 300.0);
      pattern.add(new BlockPattern(new BaseBlock(38)), 5.0);
      pattern.add(new BlockPattern(new BaseBlock(37)), 5.0);
      return pattern;
   }

   @Override
   public boolean apply(Vector position) throws WorldEditException {
      BaseBlock block = this.editSession.getBlock(position);
      if (block.getType() == 2) {
         this.editSession.setBlock(position.add(0, 1, 0), this.temperatePattern.apply(position));
         return true;
      } else if (block.getType() == 12) {
         this.editSession.setBlock(position.add(0, 1, 0), this.desertPattern.apply(position));
         return true;
      } else {
         return false;
      }
   }
}
