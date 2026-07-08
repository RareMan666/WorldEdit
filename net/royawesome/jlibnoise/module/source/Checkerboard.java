package net.royawesome.jlibnoise.module.source;

import net.royawesome.jlibnoise.MathHelper;
import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.module.Module;

public class Checkerboard extends Module {
   public Checkerboard() {
      super(0);
   }

   @Override
   public int GetSourceModuleCount() {
      return 0;
   }

   @Override
   public double GetValue(double x, double y, double z) {
      int ix = MathHelper.floor(Utils.MakeInt32Range(x));
      int iy = MathHelper.floor(Utils.MakeInt32Range(y));
      int iz = MathHelper.floor(Utils.MakeInt32Range(z));
      return (ix & 1 ^ iy & 1 ^ iz & 1) != 0 ? -1.0 : 1.0;
   }
}
