package com.sk89q.worldedit.internal.command;

import com.google.common.base.Preconditions;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.Logging;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.util.command.parametric.AbstractInvokeListener;
import com.sk89q.worldedit.util.command.parametric.InvokeHandler;
import com.sk89q.worldedit.util.command.parametric.ParameterData;
import com.sk89q.worldedit.util.command.parametric.ParameterException;
import java.io.Closeable;
import java.lang.reflect.Method;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class CommandLoggingHandler extends AbstractInvokeListener implements InvokeHandler, Closeable {
   private final WorldEdit worldEdit;
   private final Logger logger;

   public CommandLoggingHandler(WorldEdit worldEdit, Logger logger) {
      Preconditions.checkNotNull(worldEdit);
      Preconditions.checkNotNull(logger);
      this.worldEdit = worldEdit;
      this.logger = logger;
   }

   @Override
   public void preProcess(Object object, Method method, ParameterData[] parameters, CommandContext context) throws CommandException, ParameterException {
   }

   @Override
   public void preInvoke(Object object, Method method, ParameterData[] parameters, Object[] args, CommandContext context) throws CommandException {
      Logging loggingAnnotation = method.getAnnotation(Logging.class);
      StringBuilder builder = new StringBuilder();
      Logging.LogMode logMode;
      if (loggingAnnotation == null) {
         logMode = null;
      } else {
         logMode = loggingAnnotation.value();
      }

      Actor sender = context.getLocals().get(Actor.class);
      if (sender != null) {
         if (sender instanceof Player) {
            Player player = (Player)sender;
            builder.append("WorldEdit: ").append(sender.getName());
            if (sender.isPlayer()) {
               builder.append(" (in \"").append(player.getWorld().getName()).append("\")");
            }

            builder.append(": ").append(context.getCommand());
            if (context.argsLength() > 0) {
               builder.append(" ").append(context.getJoinedStrings(0));
            }

            if (logMode != null && sender.isPlayer()) {
               Vector position = player.getPosition();
               LocalSession session = this.worldEdit.getSessionManager().get(player);
               switch (logMode) {
                  case PLACEMENT:
                     try {
                        position = session.getPlacementPosition(player);
                     } catch (IncompleteRegionException var15) {
                        break;
                     }
                  case POSITION:
                     builder.append(" - Position: ").append(position);
                     break;
                  case ALL:
                     builder.append(" - Position: ").append(position);
                  case ORIENTATION_REGION:
                     builder.append(" - Orientation: ").append(player.getCardinalDirection().name());
                  case REGION:
                     try {
                        builder.append(" - Region: ").append(session.getSelection(player.getWorld()));
                     } catch (IncompleteRegionException var14) {
                     }
               }
            }

            this.logger.info(builder.toString());
         }
      }
   }

   @Override
   public void postInvoke(Object object, Method method, ParameterData[] parameters, Object[] args, CommandContext context) throws CommandException {
   }

   @Override
   public InvokeHandler createInvokeHandler() {
      return this;
   }

   @Override
   public void close() {
      for (Handler h : this.logger.getHandlers()) {
         h.close();
      }
   }
}
