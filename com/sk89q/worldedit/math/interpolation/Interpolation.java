package com.sk89q.worldedit.math.interpolation;

import com.sk89q.worldedit.Vector;
import java.util.List;

public interface Interpolation {
   void setNodes(List<Node> var1);

   Vector getPosition(double var1);

   Vector get1stDerivative(double var1);

   double arcLength(double var1, double var3);

   int getSegment(double var1);
}
