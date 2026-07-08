package com.sk89q.worldedit.function.visitor;

import com.google.common.base.Preconditions;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.RunContext;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public abstract class BreadthFirstSearch implements Operation {
   private final RegionFunction function;
   private final Queue<BlockVector> queue = new ArrayDeque<>();
   private final Set<BlockVector> visited = new HashSet<>();
   private final List<Vector> directions = new ArrayList<>();
   private int affected = 0;

   protected BreadthFirstSearch(RegionFunction function) {
      Preconditions.checkNotNull(function);
      this.function = function;
      this.addAxes();
   }

   protected Collection<Vector> getDirections() {
      return this.directions;
   }

   protected void addAxes() {
      this.directions.add(new Vector(0, -1, 0));
      this.directions.add(new Vector(0, 1, 0));
      this.directions.add(new Vector(-1, 0, 0));
      this.directions.add(new Vector(1, 0, 0));
      this.directions.add(new Vector(0, 0, -1));
      this.directions.add(new Vector(0, 0, 1));
   }

   protected void addDiagonal() {
      this.directions.add(new Vector(1, 0, 1));
      this.directions.add(new Vector(-1, 0, -1));
      this.directions.add(new Vector(1, 0, -1));
      this.directions.add(new Vector(-1, 0, 1));
   }

   public void visit(Vector position) {
      BlockVector blockVector = position.toBlockVector();
      if (!this.visited.contains(blockVector)) {
         this.queue.add(blockVector);
         this.visited.add(blockVector);
      }
   }

   private void visit(Vector from, Vector to) {
      BlockVector blockVector = to.toBlockVector();
      if (!this.visited.contains(blockVector)) {
         this.visited.add(blockVector);
         if (this.isVisitable(from, to)) {
            this.queue.add(blockVector);
         }
      }
   }

   protected abstract boolean isVisitable(Vector var1, Vector var2);

   public int getAffected() {
      return this.affected;
   }

   @Override
   public Operation resume(RunContext run) throws WorldEditException {
      Vector position;
      while ((position = this.queue.poll()) != null) {
         if (this.function.apply(position)) {
            this.affected++;
         }

         for (Vector dir : this.directions) {
            this.visit(position, position.add(dir));
         }
      }

      return null;
   }

   @Override
   public void cancel() {
   }

   @Override
   public void addStatusMessages(List<String> messages) {
      messages.add(this.getAffected() + " blocks affected");
   }
}
