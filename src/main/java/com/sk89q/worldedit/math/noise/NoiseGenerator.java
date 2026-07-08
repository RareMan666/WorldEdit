package com.sk89q.worldedit.math.noise;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.Vector2D;

public interface NoiseGenerator {
   float noise(Vector2D var1);

   float noise(Vector var1);
}
