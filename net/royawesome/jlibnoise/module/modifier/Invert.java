package net.royawesome.jlibnoise.module.modifier;

import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Invert extends Module {
   public Invert() {
      super(1);
   }

   @Override
   public int GetSourceModuleCount() {
      return 1;
   }

   @Override
   public double GetValue(double x, double y, double z) {
      if (this.SourceModule[0] == null) {
         throw new NoModuleException();
      } else {
         return -this.SourceModule[0].GetValue(x, y, z);
      }
   }
}
