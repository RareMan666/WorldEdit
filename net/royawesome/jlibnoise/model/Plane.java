package net.royawesome.jlibnoise.model;

import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Plane {
   Module module;

   public Plane(Module module) {
      if (module == null) {
         throw new IllegalArgumentException("module cannot be null");
      } else {
         this.module = module;
      }
   }

   public Module getModule() {
      return this.module;
   }

   public void setModule(Module module) {
      if (module == null) {
         throw new IllegalArgumentException("module cannot be null");
      } else {
         this.module = module;
      }
   }

   double getValue(double x, double z) {
      if (this.module == null) {
         throw new NoModuleException();
      } else {
         return this.module.GetValue(x, 0.0, z);
      }
   }
}
