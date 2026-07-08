package com.sk89q.worldedit.command.argument;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandLocals;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseItem;
import com.sk89q.worldedit.function.Contextual;
import com.sk89q.worldedit.function.EditContext;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.util.Direction;
import com.sk89q.worldedit.util.command.argument.CommandArgs;
import com.sk89q.worldedit.util.command.composition.SimpleCommand;
import com.sk89q.worldedit.world.World;

public class ItemUseParser extends SimpleCommand<Contextual<RegionFunction>> {
   private final ItemParser itemParser = this.addParameter(new ItemParser("item", "minecraft:dye:15"));

   public Contextual<RegionFunction> call(CommandArgs args, CommandLocals locals) throws CommandException {
      BaseItem item = this.itemParser.call(args, locals);
      return new ItemUseParser.ItemUseFactory(item);
   }

   @Override
   public String getDescription() {
      return "Applies an item";
   }

   @Override
   protected boolean testPermission0(CommandLocals locals) {
      return true;
   }

   private static final class ItemUseFactory implements Contextual<RegionFunction> {
      private final BaseItem item;

      private ItemUseFactory(BaseItem item) {
         this.item = item;
      }

      public RegionFunction createFromContext(EditContext input) {
         World world = ((EditSession)input.getDestination()).getWorld();
         return new ItemUseParser.ItemUseFunction(world, this.item);
      }

      @Override
      public String toString() {
         return "application of the item " + this.item.getType() + ":" + this.item.getData();
      }
   }

   private static final class ItemUseFunction implements RegionFunction {
      private final World world;
      private final BaseItem item;

      private ItemUseFunction(World world, BaseItem item) {
         this.world = world;
         this.item = item;
      }

      @Override
      public boolean apply(Vector position) throws WorldEditException {
         return this.world.useItem(position, this.item, Direction.UP);
      }
   }
}
