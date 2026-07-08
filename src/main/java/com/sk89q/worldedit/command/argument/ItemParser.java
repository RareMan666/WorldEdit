package com.sk89q.worldedit.command.argument;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseItem;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extension.input.InputParseException;
import com.sk89q.worldedit.extension.input.NoMatchException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.composition.SimpleCommand;
import com.sk89q.worldedit.world.World;

public class ItemParser extends SimpleCommand<BaseItem> {
   private final StringParser stringParser;

   public ItemParser(String name) {
      this.stringParser = this.addParameter(new StringParser(name, "The item name", null));
   }

   public ItemParser(String name, String defaultSuggestion) {
      this.stringParser = this.addParameter(new StringParser(name, "The item name", defaultSuggestion));
   }

   public BaseItem call(CommandArgs args, CommandLocals locals) throws CommandException {
      String itemString = this.stringParser.call(args, locals);
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
         return WorldEdit.getInstance().getItemFactory().parseFromInput(itemString, parserContext);
      } catch (NoMatchException var8) {
         throw new CommandException(var8.getMessage(), var8);
      } catch (InputParseException var9) {
         throw new CommandException(var9.getMessage(), var9);
      }
   }

   @Override
   public String getDescription() {
      return "Match an item";
   }

   @Override
   protected boolean testPermission0(CommandLocals locals) {
      return true;
   }
}
