package com.sk89q.worldedit.command.argument;

import com.sk89q.worldedit.function.Contextual;
import com.sk89q.worldedit.function.RegionFunction;
import com.sk89q.worldedit.util.command.composition.BranchingCommand;

public class RegionFunctionParser extends BranchingCommand<Contextual<? extends RegionFunction>> {
   public RegionFunctionParser() {
      super("functionTpe");
      this.putOption(new TreeGeneratorParser("treeType"), "forest");
      this.putOption(new ItemUseParser(), "item");
      this.putOption(new ReplaceParser(), "set");
   }

   @Override
   public String getDescription() {
      return "Choose a block function";
   }
}
