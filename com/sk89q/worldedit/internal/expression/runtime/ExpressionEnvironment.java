package com.sk89q.worldedit.internal.expression.runtime;

public interface ExpressionEnvironment {
   int getBlockType(double var1, double var3, double var5);

   int getBlockData(double var1, double var3, double var5);

   int getBlockTypeAbs(double var1, double var3, double var5);

   int getBlockDataAbs(double var1, double var3, double var5);

   int getBlockTypeRel(double var1, double var3, double var5);

   int getBlockDataRel(double var1, double var3, double var5);
}
