package com.sk89q.worldedit.command;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import com.sk89q.minecraft.util.commands.Logging;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.GroundFunction;
import com.sk89q.worldedit.function.generator.FloraGenerator;
import com.sk89q.worldedit.function.generator.ForestGenerator;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.NoiseFilter2D;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.Patterns;
import com.sk89q.worldedit.function.visitor.LayerVisitor;
import com.sk89q.worldedit.internal.annotation.Direction;
import com.sk89q.worldedit.internal.annotation.Selection;
import com.sk89q.worldedit.internal.expression.ExpressionException;
import com.sk89q.worldedit.math.convolution.GaussianKernel;
import com.sk89q.worldedit.math.convolution.HeightMap;
import com.sk89q.worldedit.math.convolution.HeightMapFilter;
import com.sk89q.worldedit.math.noise.RandomNoise;
import com.sk89q.worldedit.regions.ConvexPolyhedralRegion;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.regions.RegionOperationException;
import com.sk89q.worldedit.regions.Regions;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.util.command.binding.Range;
import com.sk89q.worldedit.util.command.binding.Switch;
import com.sk89q.worldedit.util.command.binding.Text;
import com.sk89q.worldedit.util.command.parametric.Optional;
import java.util.ArrayList;
import java.util.List;

public class RegionCommands {
   private final WorldEdit worldEdit;

   public RegionCommands(WorldEdit worldEdit) {
      Preconditions.checkNotNull(worldEdit);
      this.worldEdit = worldEdit;
   }

   @Command(
      aliases = "/line",
      usage = "<block> [thickness]",
      desc = "Draws a line segment between cuboid selection corners",
      help = "Draws a line segment between cuboid selection corners.\nCan only be used with cuboid selections.\nFlags:\n  -h generates only a shell",
      flags = "h",
      min = 1,
      max = 2
   )
   @CommandPermissions("worldedit.region.line")
   @Logging(Logging.LogMode.REGION)
   public void line(
      Player player,
      EditSession editSession,
      @Selection Region region,
      Pattern pattern,
      @Optional("0") @Range(min = 0.0) int thickness,
      @Switch('h') boolean shell
   ) throws WorldEditException {
      if (!(region instanceof CuboidRegion)) {
         player.printError("//line only works with cuboid selections");
      } else {
         CuboidRegion cuboidregion = (CuboidRegion)region;
         Vector pos1 = cuboidregion.getPos1();
         Vector pos2 = cuboidregion.getPos2();
         int blocksChanged = editSession.drawLine(Patterns.wrap(pattern), pos1, pos2, thickness, !shell);
         player.print(blocksChanged + " block(s) have been changed.");
      }
   }

   @Command(
      aliases = "/curve",
      usage = "<block> [thickness]",
      desc = "Draws a spline through selected points",
      help = "Draws a spline through selected points.\nCan only be used with convex polyhedral selections.\nFlags:\n  -h generates only a shell",
      flags = "h",
      min = 1,
      max = 2
   )
   @CommandPermissions("worldedit.region.curve")
   @Logging(Logging.LogMode.REGION)
   public void curve(
      Player player,
      EditSession editSession,
      @Selection Region region,
      Pattern pattern,
      @Optional("0") @Range(min = 0.0) int thickness,
      @Switch('h') boolean shell
   ) throws WorldEditException {
      if (!(region instanceof ConvexPolyhedralRegion)) {
         player.printError("//curve only works with convex polyhedral selections");
      } else {
         ConvexPolyhedralRegion cpregion = (ConvexPolyhedralRegion)region;
         List<Vector> vectors = new ArrayList<>(cpregion.getVertices());
         int blocksChanged = editSession.drawSpline(Patterns.wrap(pattern), vectors, 0.0, 0.0, 0.0, 10.0, thickness, !shell);
         player.print(blocksChanged + " block(s) have been changed.");
      }
   }

   @Command(
      aliases = {"/replace", "/re", "/rep"},
      usage = "[from-block] <to-block>",
      desc = "Replace all blocks in the selection with another",
      flags = "f",
      min = 1,
      max = 2
   )
   @CommandPermissions("worldedit.region.replace")
   @Logging(Logging.LogMode.REGION)
   public void replace(Player player, EditSession editSession, @Selection Region region, @Optional Mask from, Pattern to) throws WorldEditException {
      if (from == null) {
         from = new ExistingBlockMask(editSession);
      }

      int affected = editSession.replaceBlocks(region, from, Patterns.wrap(to));
      player.print(affected + " block(s) have been replaced.");
   }

   @Command(aliases = "/overlay", usage = "<block>", desc = "Set a block on top of blocks in the region", min = 1, max = 1)
   @CommandPermissions("worldedit.region.overlay")
   @Logging(Logging.LogMode.REGION)
   public void overlay(Player player, EditSession editSession, @Selection Region region, Pattern pattern) throws WorldEditException {
      int affected = editSession.overlayCuboidBlocks(region, Patterns.wrap(pattern));
      player.print(affected + " block(s) have been overlaid.");
   }

   @Command(aliases = {"/center", "/middle"}, usage = "<block>", desc = "Set the center block(s)", min = 1, max = 1)
   @Logging(Logging.LogMode.REGION)
   @CommandPermissions("worldedit.region.center")
   public void center(Player player, EditSession editSession, @Selection Region region, Pattern pattern) throws WorldEditException {
      int affected = editSession.center(region, Patterns.wrap(pattern));
      player.print("Center set (" + affected + " blocks changed)");
   }

   @Command(aliases = "/naturalize", usage = "", desc = "3 layers of dirt on top then rock below", min = 0, max = 0)
   @CommandPermissions("worldedit.region.naturalize")
   @Logging(Logging.LogMode.REGION)
   public void naturalize(Player player, EditSession editSession, @Selection Region region) throws WorldEditException {
      int affected = editSession.naturalizeCuboidBlocks(region);
      player.print(affected + " block(s) have been made to look more natural.");
   }

   @Command(aliases = "/walls", usage = "<block>", desc = "Build the four sides of the selection", min = 1, max = 1)
   @CommandPermissions("worldedit.region.walls")
   @Logging(Logging.LogMode.REGION)
   public void walls(Player player, EditSession editSession, @Selection Region region, Pattern pattern) throws WorldEditException {
      int affected = editSession.makeCuboidWalls(region, Patterns.wrap(pattern));
      player.print(affected + " block(s) have been changed.");
   }

   @Command(aliases = {"/faces", "/outline"}, usage = "<block>", desc = "Build the walls, ceiling, and floor of a selection", min = 1, max = 1)
   @CommandPermissions("worldedit.region.faces")
   @Logging(Logging.LogMode.REGION)
   public void faces(Player player, EditSession editSession, @Selection Region region, Pattern pattern) throws WorldEditException {
      int affected = editSession.makeCuboidFaces(region, Patterns.wrap(pattern));
      player.print(affected + " block(s) have been changed.");
   }

   @Command(
      aliases = "/smooth",
      usage = "[iterations]",
      flags = "n",
      desc = "Smooth the elevation in the selection",
      help = "Smooths the elevation in the selection.\nThe -n flag makes it only consider naturally occuring blocks.",
      min = 0,
      max = 1
   )
   @CommandPermissions("worldedit.region.smooth")
   @Logging(Logging.LogMode.REGION)
   public void smooth(Player player, EditSession editSession, @Selection Region region, @Optional("1") int iterations, @Switch('n') boolean affectNatural) throws WorldEditException {
      HeightMap heightMap = new HeightMap(editSession, region, affectNatural);
      HeightMapFilter filter = new HeightMapFilter(new GaussianKernel(5, 1.0));
      int affected = heightMap.applyFilter(filter, iterations);
      player.print("Terrain's height map smoothed. " + affected + " block(s) changed.");
   }

   @Command(
      aliases = "/move",
      usage = "[count] [direction] [leave-id]",
      flags = "s",
      desc = "Move the contents of the selection",
      help = "Moves the contents of the selection.\nThe -s flag shifts the selection to the target location.\nOptionally fills the old location with <leave-id>.",
      min = 0,
      max = 3
   )
   @CommandPermissions("worldedit.region.move")
   @Logging(Logging.LogMode.ORIENTATION_REGION)
   public void move(
      Player player,
      EditSession editSession,
      LocalSession session,
      @Selection Region region,
      @Optional("1") @Range(min = 1.0) int count,
      @Optional("me") @Direction Vector direction,
      @Optional("air") BaseBlock replace,
      @Switch('s') boolean moveSelection
   ) throws WorldEditException {
      int affected = editSession.moveRegion(region, direction, count, true, replace);
      if (moveSelection) {
         try {
            region.shift(direction.multiply(count));
            session.getRegionSelector(player.getWorld()).learnChanges();
            session.getRegionSelector(player.getWorld()).explainRegionAdjust(player, session);
         } catch (RegionOperationException var11) {
            player.printError(var11.getMessage());
         }
      }

      player.print(affected + " blocks moved.");
   }

   @Command(
      aliases = "/stack",
      usage = "[count] [direction]",
      flags = "sa",
      desc = "Repeat the contents of the selection",
      help = "Repeats the contents of the selection.\nFlags:\n  -s shifts the selection to the last stacked copy\n  -a skips air blocks",
      min = 0,
      max = 2
   )
   @CommandPermissions("worldedit.region.stack")
   @Logging(Logging.LogMode.ORIENTATION_REGION)
   public void stack(
      Player player,
      EditSession editSession,
      LocalSession session,
      @Selection Region region,
      @Optional("1") @Range(min = 1.0) int count,
      @Optional("me") @Direction Vector direction,
      @Switch('s') boolean moveSelection,
      @Switch('a') boolean ignoreAirBlocks
   ) throws WorldEditException {
      int affected = editSession.stackCuboidRegion(region, direction, count, !ignoreAirBlocks);
      if (moveSelection) {
         try {
            Vector size = region.getMaximumPoint().subtract(region.getMinimumPoint());
            Vector shiftVector = direction.multiply(count * (Math.abs(direction.dot(size)) + 1.0));
            region.shift(shiftVector);
            session.getRegionSelector(player.getWorld()).learnChanges();
            session.getRegionSelector(player.getWorld()).explainRegionAdjust(player, session);
         } catch (RegionOperationException var12) {
            player.printError(var12.getMessage());
         }
      }

      player.print(affected + " blocks changed. Undo with //undo");
   }

   @Command(
      aliases = "/regen",
      usage = "",
      desc = "Regenerates the contents of the selection",
      help = "Regenerates the contents of the current selection.\nThis command might affect things outside the selection,\nif they are within the same chunk.",
      min = 0,
      max = 0
   )
   @CommandPermissions("worldedit.regen")
   @Logging(Logging.LogMode.REGION)
   public void regenerateChunk(Player player, LocalSession session, EditSession editSession, @Selection Region region) throws WorldEditException {
      Mask mask = session.getMask();

      try {
         session.setMask((Mask)null);
         player.getWorld().regenerate(region, editSession);
      } finally {
         session.setMask(mask);
      }

      player.print("Region regenerated.");
   }

   @Command(
      aliases = "/deform",
      usage = "<expression>",
      desc = "Deforms a selected region with an expression",
      help = "Deforms a selected region with an expression\nThe expression is executed for each block and is expected\nto modify the variables x, y and z to point to a new block\nto fetch. See also tinyurl.com/wesyntax.",
      flags = "ro",
      min = 1,
      max = -1
   )
   @CommandPermissions("worldedit.region.deform")
   @Logging(Logging.LogMode.ALL)
   public void deform(
      Player player,
      LocalSession session,
      EditSession editSession,
      @Selection Region region,
      @Text String expression,
      @Switch('r') boolean useRawCoords,
      @Switch('o') boolean offset
   ) throws WorldEditException {
      Vector zero;
      Vector unit;
      if (useRawCoords) {
         zero = Vector.ZERO;
         unit = Vector.ONE;
      } else if (offset) {
         zero = session.getPlacementPosition(player);
         unit = Vector.ONE;
      } else {
         Vector min = region.getMinimumPoint();
         Vector max = region.getMaximumPoint();
         zero = max.add(min).multiply(0.5);
         unit = max.subtract(zero);
         if (unit.getX() == 0.0) {
            unit = unit.setX(1.0);
         }

         if (unit.getY() == 0.0) {
            unit = unit.setY(1.0);
         }

         if (unit.getZ() == 0.0) {
            unit = unit.setZ(1.0);
         }
      }

      try {
         int affected = editSession.deformRegion(region, zero, unit, expression);
         player.findFreePosition();
         player.print(affected + " block(s) have been deformed.");
      } catch (ExpressionException var12) {
         player.printError(var12.getMessage());
      }
   }

   @Command(
      aliases = "/hollow",
      usage = "[<thickness>[ <block>]]",
      desc = "Hollows out the object contained in this selection",
      help = "Hollows out the object contained in this selection.\nOptionally fills the hollowed out part with the given block.\nThickness is measured in manhattan distance.",
      min = 0,
      max = 2
   )
   @CommandPermissions("worldedit.region.hollow")
   @Logging(Logging.LogMode.REGION)
   public void hollow(
      Player player, EditSession editSession, @Selection Region region, @Optional("0") @Range(min = 0.0) int thickness, @Optional("air") Pattern pattern
   ) throws WorldEditException {
      int affected = editSession.hollowOutRegion(region, thickness, Patterns.wrap(pattern));
      player.print(affected + " block(s) have been changed.");
   }

   @Command(aliases = "/forest", usage = "[type] [density]", desc = "Make a forest within the region", min = 0, max = 2)
   @CommandPermissions("worldedit.region.forest")
   @Logging(Logging.LogMode.REGION)
   public void forest(
      Player player,
      EditSession editSession,
      @Selection Region region,
      @Optional("tree") TreeGenerator.TreeType type,
      @Optional("5") @Range(min = 0.0, max = 100.0) double density
   ) throws WorldEditException {
      density /= 100.0;
      ForestGenerator generator = new ForestGenerator(editSession, new TreeGenerator(type));
      GroundFunction ground = new GroundFunction(new ExistingBlockMask(editSession), generator);
      LayerVisitor visitor = new LayerVisitor(Regions.asFlatRegion(region), Regions.minimumBlockY(region), Regions.maximumBlockY(region), ground);
      visitor.setMask(new NoiseFilter2D(new RandomNoise(), density));
      Operations.completeLegacy(visitor);
      player.print(ground.getAffected() + " trees created.");
   }

   @Command(aliases = "/flora", usage = "[density]", desc = "Make flora within the region", min = 0, max = 1)
   @CommandPermissions("worldedit.region.flora")
   @Logging(Logging.LogMode.REGION)
   public void flora(Player player, EditSession editSession, @Selection Region region, @Optional("10") @Range(min = 0.0, max = 100.0) double density) throws WorldEditException {
      density /= 100.0;
      FloraGenerator generator = new FloraGenerator(editSession);
      GroundFunction ground = new GroundFunction(new ExistingBlockMask(editSession), generator);
      LayerVisitor visitor = new LayerVisitor(Regions.asFlatRegion(region), Regions.minimumBlockY(region), Regions.maximumBlockY(region), ground);
      visitor.setMask(new NoiseFilter2D(new RandomNoise(), density));
      Operations.completeLegacy(visitor);
      player.print(ground.getAffected() + " flora created.");
   }
}
