package net.royawesome.jlibnoise.model;

import net.royawesome.jlibnoise.MathHelper;
import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Cylinder {
   Module module;

   public Cylinder(Module mod) {
      this.module = mod;
   }

   public Module getModule() {
      return this.module;
   }

   public void setModule(Module mod) {
      if (mod == null) {
         throw new IllegalArgumentException("Mod cannot be null");
      } else {
         this.module = mod;
      }
   }

   double getValue(double angle, double height) {
      if (this.module == null) {
         throw new NoModuleException();
      } else {
         double x = MathHelper.cos(angle * (Math.PI / 180.0));
         double z = MathHelper.sin(angle * (Math.PI / 180.0));
         return this.module.GetValue(x, height, z);
      }
   }
}
