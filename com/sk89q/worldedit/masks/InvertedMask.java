package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;

@Deprecated
public class InvertedMask extends AbstractMask {
   private final Mask mask;

   public InvertedMask(Mask mask) {
      this.mask = mask;
   }

   @Override
   public void prepare(LocalSession session, LocalPlayer player, Vector target) {
      this.mask.prepare(session, player, target);
   }

   @Override
   public boolean matches(EditSession editSession, Vector position) {
      return !this.mask.matches(editSession, position);
   }

   public Mask getInvertedMask() {
      return this.mask;
   }
}
