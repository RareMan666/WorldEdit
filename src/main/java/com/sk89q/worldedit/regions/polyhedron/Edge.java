package com.sk89q.worldedit.regions.polyhedron;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.Vector;

public class Edge {
   private final Vector start;
   private final Vector end;

   public Edge(Vector start, Vector end) {
      Preconditions.checkNotNull(start);
      Preconditions.checkNotNull(end);
      this.start = start;
      this.end = end;
   }

   @Override
   public boolean equals(Object other) {
      if (!(other instanceof Edge)) {
         return false;
      } else {
         Edge otherEdge = (Edge)other;
         return this.start == otherEdge.end && this.end == otherEdge.start ? true : this.end == otherEdge.end && this.start == otherEdge.start;
      }
   }

   @Override
   public int hashCode() {
      return this.start.hashCode() ^ this.end.hashCode();
   }

   @Override
   public String toString() {
      return "(" + this.start + "," + this.end + ")";
   }

   public Triangle createTriangle(Vector vertex) {
      Preconditions.checkNotNull(vertex);
      return new Triangle(this.start, this.end, vertex);
   }

   public Triangle createTriangle2(Vector vertex) {
      Preconditions.checkNotNull(vertex);
      return new Triangle(this.start, vertex, this.end);
   }
}
