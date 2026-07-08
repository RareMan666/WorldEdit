package net.royawesome.jlibnoise.module.modifier;

import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Abs extends Module {
   public Abs() {
      super(1);
   }

   @Override
   public int GetSourceModuleCount() {
      return 1;
   }

   @Override
   public double GetValue(double x, double y, double z) {
      if (this.SourceModule == null) {
         throw new NoModuleException();
      } else {
         return Math.abs(this.SourceModule[0].GetValue(x, y, z));
      }
   }
}
