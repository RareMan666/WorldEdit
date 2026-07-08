package com.sk89q.worldedit.function;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.entity.Entity;

public interface EntityFunction {
   boolean apply(Entity var1) throws WorldEditException;
}
