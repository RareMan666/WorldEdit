package com.sk89q.worldedit.command.argument;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.NoMatchException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.composition.SimpleCommand;
import com.sk89q.worldedit.world.World;

public class PatternParser extends SimpleCommand<Pattern> {
   private final StringParser stringParser;

   public PatternParser(String name) {
      this.stringParser = this.addParameter(new StringParser(name, "The pattern"));
   }

   public Pattern call(CommandArgs args, CommandLocals locals) throws CommandException {
      String patternString = this.stringParser.call(args, locals);
      Actor actor = locals.get(Actor.class);
      LocalSession session = WorldEdit.getInstance().getSessionManager().get(actor);
      ParserContext parserContext = new ParserContext();
      parserContext.setActor(actor);
      if (actor instanceof Entity) {
         Extent extent = ((Entity)actor).getExtent();
         if (extent instanceof World) {
            parserContext.setWorld((World)extent);
         }
      }

      parserContext.setSession(session);

      try {
         return WorldEdit.getInstance().getPatternFactory().parseFromInput(patternString, parserContext);
      } catch (NoMatchException var8) {
         throw new CommandException(var8.getMessage(), var8);
      } catch (InputParseException var9) {
         throw new CommandException(var9.getMessage(), var9);
      }
   }

   @Override
   public String getDescription() {
      return "Choose a pattern";
   }

   @Override
   public boolean testPermission0(CommandLocals locals) {
      return true;
   }
}
