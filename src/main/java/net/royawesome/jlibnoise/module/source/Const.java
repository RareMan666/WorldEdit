package net.royawesome.jlibnoise.module.source;

import net.royawesome.jlibnoise.module.Module;

public class Const extends Module {
   double value = 0.0;

   public Const() {
      super(0);
   }

   public double getValue() {
      return this.value;
   }

   public void setValue(double value) {
      this.value = value;
   }

   @Override
   public int GetSourceModuleCount() {
      return 0;
   }

   @Override
   public double GetValue(double x, double y, double z) {
      return this.value;
   }
}
