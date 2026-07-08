package net.royawesome.jlibnoise.module.combiner;

import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Blend extends Module {
   public Blend() {
      super(3);
   }

   public Module getControlModule() {
      if (this.SourceModule[2] == null) {
         throw new NoModuleException();
      } else {
         return this.SourceModule[2];
      }
   }

   public void setControlModule(Module module) {
      if (module == null) {
         throw new IllegalArgumentException("Control Module cannot be null");
      } else {
         this.SourceModule[2] = module;
      }
   }

   @Override
   public int GetSourceModuleCount() {
      return 3;
   }

   @Override
   public double GetValue(double x, double y, double z) {
      if (this.SourceModule[0] == null) {
         throw new NoModuleException();
      } else if (this.SourceModule[1] == null) {
         throw new NoModuleException();
      } else if (this.SourceModule[2] == null) {
         throw new NoModuleException();
      } else {
         double v0 = this.SourceModule[0].GetValue(x, y, z);
         double v1 = this.SourceModule[1].GetValue(x, y, z);
         double alpha = (this.SourceModule[2].GetValue(x, y, z) + 1.0) / 2.0;
         return Utils.LinearInterp(v0, v1, alpha);
      }
   }
}
