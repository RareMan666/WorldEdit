package com.sk89q.minecraft.util.commands;

import com.sk89q.util.StringUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class CommandsManager<T> {
   protected static final Logger logger = Logger.getLogger(CommandsManager.class.getCanonicalName());
   protected Map<Method, Map<String, Method>> commands = new HashMap<>();
   protected Map<Method, Object> instances = new HashMap<>();
   protected Map<String, String> descs = new HashMap<>();
   protected Injector injector;
   protected Map<String, String> helpMessages = new HashMap<>();

   public void register(Class<?> cls) {
      this.registerMethods(cls, null);
   }

   public List<Command> registerAndReturn(Class<?> cls) {
      return this.registerMethods(cls, null);
   }

   public List<Command> registerMethods(Class<?> cls, Method parent) {
      try {
         if (this.getInjector() == null) {
            return this.registerMethods(cls, parent, null);
         }

         Object obj = this.getInjector().getInstance(cls);
         return this.registerMethods(cls, parent, obj);
      } catch (InvocationTargetException var4) {
         logger.log(Level.SEVERE, "Failed to register commands", (Throwable)var4);
      } catch (IllegalAccessException var5) {
         logger.log(Level.SEVERE, "Failed to register commands", (Throwable)var5);
      } catch (InstantiationException var6) {
         logger.log(Level.SEVERE, "Failed to register commands", (Throwable)var6);
      }

      return null;
   }

   private List<Command> registerMethods(Class<?> cls, Method parent, Object obj) {
      List<Command> registered = new ArrayList<>();
      Map<String, Method> map;
      if (this.commands.containsKey(parent)) {
         map = this.commands.get(parent);
      } else {
         map = new HashMap<>();
         this.commands.put(parent, map);
      }

      for (Method method : cls.getMethods()) {
         if (method.isAnnotationPresent(Command.class)) {
            boolean isStatic = Modifier.isStatic(method.getModifiers());
            Command cmd = method.getAnnotation(Command.class);

            for (String alias : cmd.aliases()) {
               map.put(alias, method);
            }

            if (!isStatic) {
               if (obj == null) {
                  continue;
               }

               this.instances.put(method, obj);
            }

            if (parent == null) {
               String commandName = cmd.aliases()[0];
               String desc = cmd.desc();
               String usage = cmd.usage();
               if (usage.isEmpty()) {
                  this.descs.put(commandName, desc);
               } else {
                  this.descs.put(commandName, usage + " - " + desc);
               }

               String help = cmd.help();
               if (help.isEmpty()) {
                  help = desc;
               }

               CharSequence arguments = this.getArguments(cmd);

               for (String alias : cmd.aliases()) {
                  String helpMessage = "/" + alias + " " + arguments + "\n\n" + help;
                  String key = alias.replaceAll("/", "");
                  String previous = this.helpMessages.put(key, helpMessage);
                  if (previous != null && !previous.replaceAll("^/[^ ]+ ", "").equals(helpMessage.replaceAll("^/[^ ]+ ", ""))) {
                     this.helpMessages.put(key, previous + "\n\n" + helpMessage);
                  }
               }
            }

            registered.add(cmd);
            if (method.isAnnotationPresent(NestedCommand.class)) {
               NestedCommand nestedCmd = method.getAnnotation(NestedCommand.class);

               for (Class<?> nestedCls : nestedCmd.value()) {
                  this.registerMethods(nestedCls, method);
               }
            }
         }
      }

      if (cls.getSuperclass() != null) {
         this.registerMethods(cls.getSuperclass(), parent, obj);
      }

      return registered;
   }

   public boolean hasCommand(String command) {
      return this.commands.get(null).containsKey(command.toLowerCase());
   }

   public Map<String, String> getCommands() {
      return this.descs;
   }

   public Map<Method, Map<String, Method>> getMethods() {
      return this.commands;
   }

   public Map<String, String> getHelpMessages() {
      return this.helpMessages;
   }

   protected String getUsage(String[] args, int level, Command cmd) {
      StringBuilder command = new StringBuilder();
      command.append('/');

      for (int i = 0; i <= level; i++) {
         command.append(args[i]);
         command.append(' ');
      }

      command.append(this.getArguments(cmd));
      String help = cmd.help();
      if (!help.isEmpty()) {
         command.append("\n\n");
         command.append(help);
      }

      return command.toString();
   }

   protected CharSequence getArguments(Command cmd) {
      String flags = cmd.flags();
      StringBuilder command2 = new StringBuilder();
      if (!flags.isEmpty()) {
         String flagString = flags.replaceAll(".:", "");
         if (!flagString.isEmpty()) {
            command2.append("[-");

            for (int i = 0; i < flagString.length(); i++) {
               command2.append(flagString.charAt(i));
            }

            command2.append("] ");
         }
      }

      command2.append(cmd.usage());
      return command2;
   }

   protected String getNestedUsage(String[] args, int level, Method method, T player) throws CommandException {
      StringBuilder command = new StringBuilder();
      command.append("/");

      for (int i = 0; i <= level; i++) {
         command.append(args[i]).append(" ");
      }

      Map<String, Method> map = this.commands.get(method);
      boolean found = false;
      command.append("<");
      Set<String> allowedCommands = new HashSet<>();

      for (Entry<String, Method> entry : map.entrySet()) {
         Method childMethod = entry.getValue();
         found = true;
         if (this.hasPermission(childMethod, player)) {
            Command childCmd = childMethod.getAnnotation(Command.class);
            allowedCommands.add(childCmd.aliases()[0]);
         }
      }

      if (!allowedCommands.isEmpty()) {
         command.append(StringUtil.joinString(allowedCommands, "|", 0));
      } else {
         if (found) {
            throw new CommandPermissionsException();
         }

         command.append("?");
      }

      command.append(">");
      return command.toString();
   }

   public void execute(String cmd, String[] args, T player, Object... methodArgs) throws CommandException {
      String[] newArgs = new String[args.length + 1];
      System.arraycopy(args, 0, newArgs, 1, args.length);
      newArgs[0] = cmd;
      Object[] newMethodArgs = new Object[methodArgs.length + 1];
      System.arraycopy(methodArgs, 0, newMethodArgs, 1, methodArgs.length);
      this.executeMethod(null, newArgs, player, newMethodArgs, 0);
   }

   public void execute(String[] args, T player, Object... methodArgs) throws CommandException {
      Object[] newMethodArgs = new Object[methodArgs.length + 1];
      System.arraycopy(methodArgs, 0, newMethodArgs, 1, methodArgs.length);
      this.executeMethod(null, args, player, newMethodArgs, 0);
   }

   public void executeMethod(Method parent, String[] args, T player, Object[] methodArgs, int level) throws CommandException {
      String cmdName = args[level];
      Map<String, Method> map = this.commands.get(parent);
      Method method = map.get(cmdName.toLowerCase());
      if (method == null) {
         if (parent == null) {
            throw new UnhandledCommandException();
         } else {
            throw new MissingNestedCommandException("Unknown command: " + cmdName, this.getNestedUsage(args, level - 1, parent, player));
         }
      } else {
         this.checkPermission(player, method);
         int argsCount = args.length - 1 - level;
         boolean executeNested = method.isAnnotationPresent(NestedCommand.class) && (argsCount > 0 || !method.getAnnotation(NestedCommand.class).executeBody());
         if (executeNested) {
            if (argsCount == 0) {
               throw new MissingNestedCommandException("Sub-command required.", this.getNestedUsage(args, level, method, player));
            }

            this.executeMethod(method, args, player, methodArgs, level + 1);
         } else if (method.isAnnotationPresent(CommandAlias.class)) {
            CommandAlias aCmd = method.getAnnotation(CommandAlias.class);
            this.executeMethod(parent, aCmd.value(), player, methodArgs, level);
         } else {
            Command cmd = method.getAnnotation(Command.class);
            String[] newArgs = new String[args.length - level];
            System.arraycopy(args, level, newArgs, 0, args.length - level);
            Set<Character> valueFlags = new HashSet<>();
            char[] flags = cmd.flags().toCharArray();
            Set<Character> newFlags = new HashSet<>();

            for (int i = 0; i < flags.length; i++) {
               if (flags.length > i + 1 && flags[i + 1] == ':') {
                  valueFlags.add(flags[i]);
                  i++;
               }

               newFlags.add(flags[i]);
            }

            CommandContext context = new CommandContext(newArgs, valueFlags);
            if (context.argsLength() < cmd.min()) {
               throw new CommandUsageException("Too few arguments.", this.getUsage(args, level, cmd));
            }

            if (cmd.max() != -1 && context.argsLength() > cmd.max()) {
               throw new CommandUsageException("Too many arguments.", this.getUsage(args, level, cmd));
            }

            if (!cmd.anyFlags()) {
               for (char flag : context.getFlags()) {
                  if (!newFlags.contains(flag)) {
                     throw new CommandUsageException("Unknown flag: " + flag, this.getUsage(args, level, cmd));
                  }
               }
            }

            methodArgs[0] = context;
            Object instance = this.instances.get(method);
            this.invokeMethod(parent, args, player, method, instance, methodArgs, argsCount);
         }
      }
   }

   protected void checkPermission(T player, Method method) throws CommandException {
      if (!this.hasPermission(method, player)) {
         throw new CommandPermissionsException();
      }
   }

   public void invokeMethod(Method parent, String[] args, T player, Method method, Object instance, Object[] methodArgs, int level) throws CommandException {
      try {
         method.invoke(instance, methodArgs);
      } catch (IllegalArgumentException var9) {
         logger.log(Level.SEVERE, "Failed to execute command", (Throwable)var9);
      } catch (IllegalAccessException var10) {
         logger.log(Level.SEVERE, "Failed to execute command", (Throwable)var10);
      } catch (InvocationTargetException var11) {
         if (var11.getCause() instanceof CommandException) {
            throw (CommandException)var11.getCause();
         }

         throw new WrappedCommandException(var11.getCause());
      }
   }

   protected boolean hasPermission(Method method, T player) {
      CommandPermissions perms = method.getAnnotation(CommandPermissions.class);
      if (perms == null) {
         return true;
      } else {
         for (String perm : perms.value()) {
            if (this.hasPermission(player, perm)) {
               return true;
            }
         }

         return false;
      }
   }

   public abstract boolean hasPermission(T var1, String var2);

   public Injector getInjector() {
      return this.injector;
   }

   public void setInjector(Injector injector) {
      this.injector = injector;
   }
}
