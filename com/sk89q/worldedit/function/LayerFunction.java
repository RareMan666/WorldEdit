package com.sk89q.worldedit.function;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;

public interface LayerFunction {
   boolean isGround(Vector var1);

   boolean apply(Vector var1, int var2) throws WorldEditException;
}
