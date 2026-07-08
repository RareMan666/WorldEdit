package com.sk89q.worldedit.math.geom;

import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector2D;
import java.util.ArrayList;
import java.util.List;

public final class Polygons {
   private Polygons() {
   }

   public static List<BlockVector2D> polygonizeCylinder(Vector2D center, Vector2D radius, int maxPoints) {
      int nPoints = (int)Math.ceil(Math.PI * radius.length());
      if (maxPoints >= 0 && nPoints >= maxPoints) {
         nPoints = maxPoints - 1;
      }

      List<BlockVector2D> points = new ArrayList<>(nPoints);

      for (int i = 0; i < nPoints; i++) {
         double angle = i * (Math.PI * 2) / nPoints;
         Vector2D pos = new Vector2D(Math.cos(angle), Math.sin(angle));
         BlockVector2D blockVector2D = pos.multiply(radius).add(center).toBlockVector2D();
         points.add(blockVector2D);
      }

      return points;
   }
}
