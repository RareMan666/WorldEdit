package com.sk89q.worldedit.bukkit.adapter;

import com.sk89q.worldedit.util.io.Closer;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BukkitImplLoader {
   private static final Logger log = Logger.getLogger(BukkitImplLoader.class.getCanonicalName());
   private final List<String> adapterCandidates = new ArrayList<>();
   private String customCandidate;
   private static final String SEARCH_PACKAGE = "com.sk89q.worldedit.bukkit.adapter.impl";
   private static final String SEARCH_PACKAGE_DOT = "com.sk89q.worldedit.bukkit.adapter.impl.";
   private static final String SEARCH_PATH = "com.sk89q.worldedit.bukkit.adapter.impl".replace(".", "/");
   private static final String CLASS_SUFFIX = ".class";
   private static final String LOAD_ERROR_MESSAGE = "\n**********************************************\n** This WorldEdit version does not fully support your version of Bukkit.\n**\n** When working with blocks or undoing, chests will be empty, signs\n** will be blank, and so on. There will be no support for entity\n** and biome-related functions.\n**\n** Please see http://wiki.sk89q.com/wiki/WorldEdit/Bukkit_adapters\n**********************************************\n";

   public BukkitImplLoader() {
      this.addDefaults();
   }

   private void addDefaults() {
      String className = System.getProperty("worldedit.bukkit.adapter");
      if (className != null) {
         this.customCandidate = className;
         this.adapterCandidates.add(className);
         log.log(Level.INFO, "-Dworldedit.bukkit.adapter used to add " + className + " to the list of available Bukkit adapters");
      }
   }

   public void addFromJar(File file) throws IOException {
      Closer closer = Closer.create();
      JarFile jar = closer.register(new JarFile(file));

      try {
         Enumeration entries = jar.entries();

         while (entries.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry)entries.nextElement();
            String className = jarEntry.getName().replaceAll("[/\\\\]+", ".");
            if (className.startsWith("com.sk89q.worldedit.bukkit.adapter.impl.") && !jarEntry.isDirectory()) {
               int beginIndex = 0;
               int endIndex = className.length() - ".class".length();
               className = className.substring(beginIndex, endIndex);
               this.adapterCandidates.add(className);
            }
         }
      } finally {
         closer.close();
      }
   }

   public void addFromPath(ClassLoader classLoader) throws IOException {
      Enumeration<URL> resources = classLoader.getResources(SEARCH_PATH);

      while (resources.hasMoreElements()) {
         File file = new File(resources.nextElement().getFile());
         this.addFromPath(file);
      }
   }

   private void addFromPath(File file) {
      String resource = "com.sk89q.worldedit.bukkit.adapter.impl." + file.getName();
      if (file.isDirectory()) {
         File[] files = file.listFiles();
         if (files != null) {
            for (File child : files) {
               this.addFromPath(child);
            }
         }
      } else if (resource.endsWith(".class")) {
         int beginIndex = 0;
         int endIndex = resource.length() - ".class".length();
         String className = resource.substring(beginIndex, endIndex);
         this.adapterCandidates.add(className);
      }
   }

   public BukkitImplAdapter loadAdapter() throws AdapterLoadException {
      for (String className : this.adapterCandidates) {
         try {
            Class<?> cls = Class.forName(className);
            if (BukkitImplAdapter.class.isAssignableFrom(cls)) {
               return (BukkitImplAdapter)cls.newInstance();
            }

            log.log(
               Level.WARNING,
               "Failed to load the Bukkit adapter class '" + className + "' because it does not implement " + BukkitImplAdapter.class.getCanonicalName()
            );
         } catch (ClassNotFoundException var4) {
            log.log(Level.WARNING, "Failed to load the Bukkit adapter class '" + className + "' that is not supposed to be missing", (Throwable)var4);
         } catch (IllegalAccessException var5) {
            log.log(Level.WARNING, "Failed to load the Bukkit adapter class '" + className + "' that is not supposed to be raising this error", (Throwable)var5);
         } catch (Throwable var6) {
            if (className.equals(this.customCandidate)) {
               log.log(Level.WARNING, "Failed to load the Bukkit adapter class '" + className + "'", var6);
            }
         }
      }

      throw new AdapterLoadException(
         "\n**********************************************\n** This WorldEdit version does not fully support your version of Bukkit.\n**\n** When working with blocks or undoing, chests will be empty, signs\n** will be blank, and so on. There will be no support for entity\n** and biome-related functions.\n**\n** Please see http://wiki.sk89q.com/wiki/WorldEdit/Bukkit_adapters\n**********************************************\n"
      );
   }
}
