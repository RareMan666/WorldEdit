package com.sk89q.worldedit.masks;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalPlayer;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import java.util.ArrayList;
import java.util.List;

@Deprecated
public class CombinedMask extends AbstractMask {
   private final List<Mask> masks = new ArrayList<>();

   public CombinedMask() {
   }

   public CombinedMask(Mask mask) {
      this.add(mask);
   }

   public CombinedMask(Mask... mask) {
      for (Mask m : mask) {
         this.add(m);
      }
   }

   public CombinedMask(List<Mask> masks) {
      this.masks.addAll(masks);
   }

   public void add(Mask mask) {
      this.masks.add(mask);
   }

   public boolean remove(Mask mask) {
      return this.masks.remove(mask);
   }

   public boolean has(Mask mask) {
      return this.masks.contains(mask);
   }

   @Override
   public void prepare(LocalSession session, LocalPlayer player, Vector target) {
      for (Mask mask : this.masks) {
         mask.prepare(session, player, target);
      }
   }

   @Override
   public boolean matches(EditSession editSession, Vector position) {
      for (Mask mask : this.masks) {
         if (!mask.matches(editSession, position)) {
            return false;
         }
      }

      return true;
   }
}
