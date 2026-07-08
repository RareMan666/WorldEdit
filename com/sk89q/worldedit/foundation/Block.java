package com.sk89q.worldedit.foundation;

@Deprecated
public abstract class Block {
   public abstract int getId();

   public abstract void setId(int var1);

   public abstract int getData();

   public abstract void setData(int var1);

   public abstract void setIdAndData(int var1, int var2);

   public abstract boolean hasWildcardData();
}
