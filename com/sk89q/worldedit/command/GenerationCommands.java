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
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.function.pattern.Patterns;
import com.sk89q.worldedit.internal.annotation.Selection;
import com.sk89q.worldedit.internal.expression.ExpressionException;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.util.command.binding.Range;
import com.sk89q.worldedit.util.command.binding.Switch;
import com.sk89q.worldedit.util.command.binding.Text;
import com.sk89q.worldedit.util.command.parametric.Optional;
import com.sk89q.worldedit.world.biome.BaseBiome;

public class GenerationCommands {
   private final WorldEdit worldEdit;

   public GenerationCommands(WorldEdit worldEdit) {
      Preconditions.checkNotNull(worldEdit);
      this.worldEdit = worldEdit;
   }

   @Command(
      aliases = "/hcyl",
      usage = "<pattern> <radius>[,<radius>] [height]",
      desc = "Generates a hollow cylinder.",
      help = "Generates a hollow cylinder.\nBy specifying 2 radii, separated by a comma,\nyou can generate elliptical cylinders.\nThe 1st radius is north/south, the 2nd radius is east/west.",
      min = 2,
      max = 3
   )
   @CommandPermissions("worldedit.generation.cylinder")
   @Logging(Logging.LogMode.PLACEMENT)
   public void hcyl(Player player, LocalSession session, EditSession editSession, Pattern pattern, String radiusString, @Optional("1") int height) throws WorldEditException {
      this.cyl(player, session, editSession, pattern, radiusString, height, true);
   }

   @Command(
      aliases = "/cyl",
      usage = "<block> <radius>[,<radius>] [height]",
      flags = "h",
      desc = "Generates a cylinder.",
      help = "Generates a cylinder.\nBy specifying 2 radii, separated by a comma,\nyou can generate elliptical cylinders.\nThe 1st radius is north/south, the 2nd radius is east/west.",
      min = 2,
      max = 3
   )
   @CommandPermissions("worldedit.generation.cylinder")
   @Logging(Logging.LogMode.PLACEMENT)
   public void cyl(
      Player player,
      LocalSession session,
      EditSession editSession,
      Pattern pattern,
      String radiusString,
      @Optional("1") int height,
      @Switch('h') boolean hollow
   ) throws WorldEditException {
      String[] radii = radiusString.split(",");
      double radiusZ;
      double radiusX;
      switch (radii.length) {
         case 1:
            radiusX = radiusZ = Math.max(1.0, Double.parseDouble(radii[0]));
            break;
         case 2:
            radiusX = Math.max(1.0, Double.parseDouble(radii[0]));
            radiusZ = Math.max(1.0, Double.parseDouble(radii[1]));
            break;
         default:
            player.printError("You must either specify 1 or 2 radius values.");
            return;
      }

      this.worldEdit.checkMaxRadius(radiusX);
      this.worldEdit.checkMaxRadius(radiusZ);
      this.worldEdit.checkMaxRadius(height);
      Vector pos = session.getPlacementPosition(player);
      int affected = editSession.makeCylinder(pos, Patterns.wrap(pattern), radiusX, radiusZ, height, !hollow);
      player.print(affected + " block(s) have been created.");
   }

   @Command(
      aliases = "/hsphere",
      usage = "<block> <radius>[,<radius>,<radius>] [raised?]",
      desc = "Generates a hollow sphere.",
      help = "Generates a hollow sphere.\nBy specifying 3 radii, separated by commas,\nyou can generate an ellipsoid. The order of the ellipsoid radii\nis north/south, up/down, east/west.",
      min = 2,
      max = 3
   )
   @CommandPermissions("worldedit.generation.sphere")
   @Logging(Logging.LogMode.PLACEMENT)
   public void hsphere(Player player, LocalSession session, EditSession editSession, Pattern pattern, String radiusString, @Optional("false") boolean raised) throws WorldEditException {
      this.sphere(player, session, editSession, pattern, radiusString, raised, true);
   }

   @Command(
      aliases = "/sphere",
      usage = "<block> <radius>[,<radius>,<radius>] [raised?]",
      flags = "h",
      desc = "Generates a filled sphere.",
      help = "Generates a filled sphere.\nBy specifying 3 radii, separated by commas,\nyou can generate an ellipsoid. The order of the ellipsoid radii\nis north/south, up/down, east/west.",
      min = 2,
      max = 3
   )
   @CommandPermissions("worldedit.generation.sphere")
   @Logging(Logging.LogMode.PLACEMENT)
   public void sphere(
      Player player,
      LocalSession session,
      EditSession editSession,
      Pattern pattern,
      String radiusString,
      @Optional("false") boolean raised,
      @Switch('h') boolean hollow
   ) throws WorldEditException {
      String[] radii = radiusString.split(",");
      double radiusZ;
      double radiusY;
      double radiusX;
      switch (radii.length) {
         case 1:
            radiusX = radiusY = radiusZ = Math.max(1.0, Double.parseDouble(radii[0]));
            break;
         case 3:
            radiusX = Math.max(1.0, Double.parseDouble(radii[0]));
            radiusY = Math.max(1.0, Double.parseDouble(radii[1]));
            radiusZ = Math.max(1.0, Double.parseDouble(radii[2]));
            break;
         default:
            player.printError("You must either specify 1 or 3 radius values.");
            return;
      }

      this.worldEdit.checkMaxRadius(radiusX);
      this.worldEdit.checkMaxRadius(radiusY);
      this.worldEdit.checkMaxRadius(radiusZ);
      Vector pos = session.getPlacementPosition(player);
      if (raised) {
         pos = pos.add(0.0, radiusY, 0.0);
      }

      int affected = editSession.makeSphere(pos, Patterns.wrap(pattern), radiusX, radiusY, radiusZ, !hollow);
      player.findFreePosition();
      player.print(affected + " block(s) have been created.");
   }

   @Command(aliases = "forestgen", usage = "[size] [type] [density]", desc = "Generate a forest", min = 0, max = 3)
   @CommandPermissions("worldedit.generation.forest")
   @Logging(Logging.LogMode.POSITION)
   public void forestGen(
      Player player,
      LocalSession session,
      EditSession editSession,
      @Optional("10") int size,
      @Optional("tree") TreeGenerator.TreeType type,
      @Optional("5") double density
   ) throws WorldEditException {
      density /= 100.0;
      int affected = editSession.makeForest(session.getPlacementPosition(player), size, density, new TreeGenerator(type));
      player.print(affected + " trees created.");
   }

   @Command(aliases = "pumpkins", usage = "[size]", desc = "Generate pumpkin patches", min = 0, max = 1)
   @CommandPermissions("worldedit.generation.pumpkins")
   @Logging(Logging.LogMode.POSITION)
   public void pumpkins(Player player, LocalSession session, EditSession editSession, @Optional("10") int apothem) throws WorldEditException {
      int affected = editSession.makePumpkinPatches(session.getPlacementPosition(player), apothem);
      player.print(affected + " pumpkin patches created.");
   }

   @Command(aliases = "/hpyramid", usage = "<block> <size>", desc = "Generate a hollow pyramid", min = 2, max = 2)
   @CommandPermissions("worldedit.generation.pyramid")
   @Logging(Logging.LogMode.PLACEMENT)
   public void hollowPyramid(Player player, LocalSession session, EditSession editSession, Pattern pattern, @Range(min = 1.0) int size) throws WorldEditException {
      this.pyramid(player, session, editSession, pattern, size, true);
   }

   @Command(aliases = "/pyramid", usage = "<block> <size>", flags = "h", desc = "Generate a filled pyramid", min = 2, max = 2)
   @CommandPermissions("worldedit.generation.pyramid")
   @Logging(Logging.LogMode.PLACEMENT)
   public void pyramid(Player player, LocalSession session, EditSession editSession, Pattern pattern, @Range(min = 1.0) int size, @Switch('h') boolean hollow) throws WorldEditException {
      Vector pos = session.getPlacementPosition(player);
      this.worldEdit.checkMaxRadius(size);
      int affected = editSession.makePyramid(pos, Patterns.wrap(pattern), size, !hollow);
      player.findFreePosition();
      player.print(affected + " block(s) have been created.");
   }

   @Command(
      aliases = {"/generate", "/gen", "/g"},
      usage = "<block> <expression>",
      desc = "Generates a shape according to a formula.",
      help = "Generates a shape according to a formula that is expected to\nreturn positive numbers (true) if the point is inside the shape\nOptionally set type/data to the desired block.\nFlags:\n  -h to generate a hollow shape\n  -r to use raw minecraft coordinates\n  -o is like -r, except offset from placement.\n  -c is like -r, except offset selection center.\nIf neither -r nor -o is given, the selection is mapped to -1..1\nSee also tinyurl.com/wesyntax.",
      flags = "hroc",
      min = 2,
      max = -1
   )
   @CommandPermissions("worldedit.generation.shape")
   @Logging(Logging.LogMode.ALL)
   public void generate(
      Player player,
      LocalSession session,
      EditSession editSession,
      @Selection Region region,
      Pattern pattern,
      @Text String expression,
      @Switch('h') boolean hollow,
      @Switch('r') boolean useRawCoords,
      @Switch('o') boolean offset,
      @Switch('c') boolean offsetCenter
   ) throws WorldEditException {
      Vector zero;
      Vector unit;
      if (useRawCoords) {
         zero = Vector.ZERO;
         unit = Vector.ONE;
      } else if (offset) {
         zero = session.getPlacementPosition(player);
         unit = Vector.ONE;
      } else if (offsetCenter) {
         Vector min = region.getMinimumPoint();
         Vector max = region.getMaximumPoint();
         zero = max.add(min).multiply(0.5);
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
         int affected = editSession.makeShape(region, zero, unit, Patterns.wrap(pattern), expression, hollow);
         player.findFreePosition();
         player.print(affected + " block(s) have been created.");
      } catch (ExpressionException var15) {
         player.printError(var15.getMessage());
      }
   }

   @Command(
      aliases = {"/generatebiome", "/genbiome", "/gb"},
      usage = "<biome> <expression>",
      desc = "Sets biome according to a formula.",
      help = "Generates a shape according to a formula that is expected to\nreturn positive numbers (true) if the point is inside the shape\nSets the biome of blocks in that shape.\nFlags:\n  -h to generate a hollow shape\n  -r to use raw minecraft coordinates\n  -o is like -r, except offset from placement.\n  -c is like -r, except offset selection center.\nIf neither -r nor -o is given, the selection is mapped to -1..1\nSee also tinyurl.com/wesyntax.",
      flags = "hroc",
      min = 2,
      max = -1
   )
   @CommandPermissions({"worldedit.generation.shape", "worldedit.biome.set"})
   @Logging(Logging.LogMode.ALL)
   public void generateBiome(
      Player player,
      LocalSession session,
      EditSession editSession,
      @Selection Region region,
      BaseBiome target,
      @Text String expression,
      @Switch('h') boolean hollow,
      @Switch('r') boolean useRawCoords,
      @Switch('o') boolean offset,
      @Switch('c') boolean offsetCenter
   ) throws WorldEditException {
      Vector zero;
      Vector unit;
      if (useRawCoords) {
         zero = Vector.ZERO;
         unit = Vector.ONE;
      } else if (offset) {
         zero = session.getPlacementPosition(player);
         unit = Vector.ONE;
      } else if (offsetCenter) {
         Vector min = region.getMinimumPoint();
         Vector max = region.getMaximumPoint();
         zero = max.add(min).multiply(0.5);
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
         int affected = editSession.makeBiomeShape(region, zero, unit, target, expression, hollow);
         player.findFreePosition();
         player.print("" + affected + " columns affected.");
      } catch (ExpressionException var15) {
         player.printError(var15.getMessage());
      }
   }
}
