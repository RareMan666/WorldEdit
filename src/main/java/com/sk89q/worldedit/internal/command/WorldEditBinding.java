package com.sk89q.worldedit.internal.command;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.UnknownDirectionException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.input.NoMatchException;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.internal.annotation.Direction;
import com.sk89q.worldedit.internal.annotation.Selection;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.TreeGenerator;
import com.sk89q.worldedit.util.command.parametric.ArgumentStack;
import com.sk89q.worldedit.util.command.parametric.BindingBehavior;
import com.sk89q.worldedit.util.command.parametric.BindingHelper;
import com.sk89q.worldedit.util.command.parametric.BindingMatch;
import com.sk89q.worldedit.util.command.parametric.ParameterException;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.biome.BaseBiome;
import com.sk89q.worldedit.world.biome.Biomes;
import com.sk89q.worldedit.world.registry.BiomeRegistry;
import java.util.Arrays;
import java.util.List;

public class WorldEditBinding extends BindingHelper {
   private final WorldEdit worldEdit;

   public WorldEditBinding(WorldEdit worldEdit) {
      this.worldEdit = worldEdit;
   }

   @BindingMatch(classifier = Selection.class, type = Region.class, behavior = BindingBehavior.PROVIDES)
   public Object getSelection(ArgumentStack context, Selection selection) throws IncompleteRegionException, ParameterException {
      Player sender = this.getPlayer(context);
      LocalSession session = this.worldEdit.getSessionManager().get(sender);
      return session.getSelection(sender.getWorld());
   }

   @BindingMatch(type = EditSession.class, behavior = BindingBehavior.PROVIDES)
   public EditSession getEditSession(ArgumentStack context) throws ParameterException {
      Player sender = this.getPlayer(context);
      LocalSession session = this.worldEdit.getSessionManager().get(sender);
      EditSession editSession = session.createEditSession(sender);
      editSession.enableQueue();
      context.getContext().getLocals().put(EditSession.class, editSession);
      session.tellVersion(sender);
      return editSession;
   }

   @BindingMatch(type = LocalSession.class, behavior = BindingBehavior.PROVIDES)
   public LocalSession getLocalSession(ArgumentStack context) throws ParameterException {
      Player sender = this.getPlayer(context);
      return this.worldEdit.getSessionManager().get(sender);
   }

   @BindingMatch(type = Actor.class, behavior = BindingBehavior.PROVIDES)
   public Actor getActor(ArgumentStack context) throws ParameterException {
      Actor sender = context.getContext().getLocals().get(Actor.class);
      if (sender == null) {
         throw new ParameterException("Missing 'Actor'");
      } else {
         return sender;
      }
   }

   @BindingMatch(type = Player.class, behavior = BindingBehavior.PROVIDES)
   public Player getPlayer(ArgumentStack context) throws ParameterException {
      Actor sender = context.getContext().getLocals().get(Actor.class);
      if (sender == null) {
         throw new ParameterException("No player to get a session for");
      } else if (sender instanceof Player) {
         return (Player)sender;
      } else {
         throw new ParameterException("Caller is not a player");
      }
   }

   @BindingMatch(type = BaseBlock.class, behavior = BindingBehavior.CONSUMES, consumedCount = 1)
   public BaseBlock getBaseBlock(ArgumentStack context) throws ParameterException, WorldEditException {
      Actor actor = context.getContext().getLocals().get(Actor.class);
      ParserContext parserContext = new ParserContext();
      parserContext.setActor(context.getContext().getLocals().get(Actor.class));
      if (actor instanceof Entity) {
         Extent extent = ((Entity)actor).getExtent();
         if (extent instanceof World) {
            parserContext.setWorld((World)extent);
         }
      }

      parserContext.setSession(this.worldEdit.getSessionManager().get(actor));

      try {
         return this.worldEdit.getBlockFactory().parseFromInput(context.next(), parserContext);
      } catch (NoMatchException var5) {
         throw new ParameterException(var5.getMessage(), var5);
      }
   }

   @BindingMatch(type = Pattern.class, behavior = BindingBehavior.CONSUMES, consumedCount = 1)
   public Pattern getPattern(ArgumentStack context) throws ParameterException, WorldEditException {
      Actor actor = context.getContext().getLocals().get(Actor.class);
      ParserContext parserContext = new ParserContext();
      parserContext.setActor(context.getContext().getLocals().get(Actor.class));
      if (actor instanceof Entity) {
         Extent extent = ((Entity)actor).getExtent();
         if (extent instanceof World) {
            parserContext.setWorld((World)extent);
         }
      }

      parserContext.setSession(this.worldEdit.getSessionManager().get(actor));

      try {
         return this.worldEdit.getPatternFactory().parseFromInput(context.next(), parserContext);
      } catch (NoMatchException var5) {
         throw new ParameterException(var5.getMessage(), var5);
      }
   }

   @BindingMatch(type = Mask.class, behavior = BindingBehavior.CONSUMES, consumedCount = 1)
   public Mask getMask(ArgumentStack context) throws ParameterException, WorldEditException {
      Actor actor = context.getContext().getLocals().get(Actor.class);
      ParserContext parserContext = new ParserContext();
      parserContext.setActor(context.getContext().getLocals().get(Actor.class));
      if (actor instanceof Entity) {
         Extent extent = ((Entity)actor).getExtent();
         if (extent instanceof World) {
            parserContext.setWorld((World)extent);
         }
      }

      parserContext.setSession(this.worldEdit.getSessionManager().get(actor));

      try {
         return this.worldEdit.getMaskFactory().parseFromInput(context.next(), parserContext);
      } catch (NoMatchException var5) {
         throw new ParameterException(var5.getMessage(), var5);
      }
   }

   @BindingMatch(classifier = Direction.class, type = Vector.class, behavior = BindingBehavior.CONSUMES, consumedCount = 1)
   public Vector getDirection(ArgumentStack context, Direction direction) throws ParameterException, UnknownDirectionException {
      Player sender = this.getPlayer(context);
      return this.worldEdit.getDirection(sender, context.next());
   }

   @BindingMatch(type = TreeGenerator.TreeType.class, behavior = BindingBehavior.CONSUMES, consumedCount = 1)
   public TreeGenerator.TreeType getTreeType(ArgumentStack context) throws ParameterException, WorldEditException {
      String input = context.next();
      if (input != null) {
         TreeGenerator.TreeType type = TreeGenerator.lookup(input);
         if (type != null) {
            return type;
         } else {
            throw new ParameterException(
               String.format("Can't recognize tree type '%s' -- choose from: %s", input, Arrays.toString((Object[])TreeGenerator.TreeType.values()))
            );
         }
      } else {
         return TreeGenerator.TreeType.TREE;
      }
   }

   @BindingMatch(type = BaseBiome.class, behavior = BindingBehavior.CONSUMES, consumedCount = 1)
   public BaseBiome getBiomeType(ArgumentStack context) throws ParameterException, WorldEditException {
      String input = context.next();
      if (input != null) {
         Actor actor = context.getContext().getLocals().get(Actor.class);
         if (actor instanceof Entity) {
            Extent extent = ((Entity)actor).getExtent();
            if (extent instanceof World) {
               World world = (World)extent;
               BiomeRegistry biomeRegistry = world.getWorldData().getBiomeRegistry();
               List<BaseBiome> knownBiomes = biomeRegistry.getBiomes();
               BaseBiome biome = Biomes.findBiomeByName(knownBiomes, input, biomeRegistry);
               if (biome != null) {
                  return biome;
               } else {
                  throw new ParameterException(String.format("Can't recognize biome type '%s' -- use /biomelist to list available types", input));
               }
            } else {
               throw new ParameterException("A world is required.");
            }
         } else {
            throw new ParameterException("An entity is required.");
         }
      } else {
         throw new ParameterException(
            "This command takes a 'default' biome if one is not set, except there is no particular biome that should be 'default', so the command should not be taking a default biome"
         );
      }
   }
}
