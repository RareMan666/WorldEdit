package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;

public abstract class Module {
   protected Module[] SourceModule = null;

   public Module(int sourceModuleCount) {
      if (sourceModuleCount > 0) {
         this.SourceModule = new Module[sourceModuleCount];

         for (int i = 0; i < sourceModuleCount; i++) {
            this.SourceModule[i] = null;
         }
      } else {
         this.SourceModule = null;
      }
   }

   public Module getSourceModule(int index) {
      if (index < this.GetSourceModuleCount() && index >= 0 && this.SourceModule[index] != null) {
         return this.SourceModule[index];
      } else {
         throw new NoModuleException();
      }
   }

   public void SetSourceModule(int index, Module sourceModule) {
      if (this.SourceModule != null) {
         if (index < this.GetSourceModuleCount() && index >= 0) {
            this.SourceModule[index] = sourceModule;
         } else {
            throw new IllegalArgumentException("Index must be between 0 and GetSourceMoudleCount()");
         }
      }
   }

   public abstract int GetSourceModuleCount();

   public abstract double GetValue(double var1, double var3, double var5);
}
