package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import java.util.Set;

@Deprecated
public class UnderOverlayMask extends AbstractMask {
   private final int yMod;
   private Mask mask;

   @Deprecated
   public UnderOverlayMask(Set<Integer> ids, boolean overlay) {
      this(new BlockTypeMask(ids), overlay);
   }

   public UnderOverlayMask(Mask mask, boolean overlay) {
      this.yMod = overlay ? -1 : 1;
      this.mask = mask;
   }

   @Deprecated
   public void addAll(Set<Integer> ids) {
      if (this.mask instanceof BlockMask) {
         BlockMask blockTypeMask = (BlockMask)this.mask;

         for (Integer id : ids) {
            blockTypeMask.add(new BaseBlock(id));
         }
      } else if (this.mask instanceof ExistingBlockMask) {
         BlockMask blockMask = new BlockMask();

         for (int type : ids) {
            blockMask.add(new BaseBlock(type));
         }

         this.mask = blockMask;
      }
   }

   @Override
   public void prepare(LocalSession session, LocalPlayer player, Vector target) {
      this.mask.prepare(session, player, target);
   }

   @Override
   public boolean matches(EditSession editSession, Vector position) {
      return !this.mask.matches(editSession, position) && this.mask.matches(editSession, position.add(0, this.yMod, 0));
   }
}
