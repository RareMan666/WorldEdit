package com.sk89q.worldedit.function.block;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.function.LayerFunction;
import com.sk89q.worldedit.masks.BlockMask;
import com.sk89q.worldedit.masks.Mask;

public class Naturalizer implements LayerFunction {
   private final EditSession editSession;
   private final BaseBlock grass = new BaseBlock(2);
   private final BaseBlock dirt = new BaseBlock(3);
   private final BaseBlock stone = new BaseBlock(1);
   private final Mask mask = new BlockMask(this.grass, this.dirt, this.stone);
   private int affected = 0;

   public Naturalizer(EditSession editSession) {
      Preconditions.checkNotNull(editSession);
      this.editSession = editSession;
   }

   public int getAffected() {
      return this.affected;
   }

   @Override
   public boolean isGround(Vector position) {
      return this.mask.matches(this.editSession, position);
   }

   @Override
   public boolean apply(Vector position, int depth) throws WorldEditException {
      if (this.mask.matches(this.editSession, position)) {
         this.affected++;
         switch (depth) {
            case 0:
               this.editSession.setBlock(position, this.grass);
               break;
            case 1:
            case 2:
            case 3:
               this.editSession.setBlock(position, this.dirt);
               break;
            default:
               this.editSession.setBlock(position, this.stone);
         }
      }

      return true;
   }
}
