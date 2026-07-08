package com.sk89q.worldedit.math.transform;

import com.sk89q.worldedit.Vector;

public interface Transform {
   boolean isIdentity();

   Vector apply(Vector var1);

   Transform inverse();

   Transform combine(Transform var1);
}
