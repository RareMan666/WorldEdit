package com.sk89q.worldedit.command.tool.brush;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.internal.LocalWorldAdapter;
import com.sk89q.worldedit.math.convolution.GaussianKernel;
import com.sk89q.worldedit.math.convolution.HeightMap;
import com.sk89q.worldedit.math.convolution.HeightMapFilter;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;

public class SmoothBrush implements Brush {
   private int iterations;
   private boolean naturalOnly;

   public SmoothBrush(int iterations) {
      this(iterations, false);
   }

   public SmoothBrush(int iterations, boolean naturalOnly) {
      this.iterations = iterations;
      this.naturalOnly = naturalOnly;
   }

   @Override
   public void build(EditSession editSession, Vector position, Pattern pattern, double size) throws MaxChangedBlocksException {
      WorldVector min = new WorldVector(LocalWorldAdapter.adapt(editSession.getWorld()), position.subtract(size, size, size));
      Vector max = position.add(size, size + 10.0, size);
      Region region = new CuboidRegion(editSession.getWorld(), min, max);
      HeightMap heightMap = new HeightMap(editSession, region, this.naturalOnly);
      HeightMapFilter filter = new HeightMapFilter(new GaussianKernel(5, 1.0));
      heightMap.applyFilter(filter, this.iterations);
   }
}
