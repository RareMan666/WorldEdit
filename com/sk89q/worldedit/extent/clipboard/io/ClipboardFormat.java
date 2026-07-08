package com.sk89q.worldedit.extent.clipboard.io;

import com.google.common.base.Preconditions;
import com.sk89q.jnbt.NBTConstants;
import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.jnbt.NBTOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.annotation.Nullable;

public enum ClipboardFormat {
   SCHEMATIC("mcedit", "mce", "schematic") {
      @Override
      public ClipboardReader getReader(InputStream inputStream) throws IOException {
         NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(inputStream));
         return new SchematicReader(nbtStream);
      }

      @Override
      public ClipboardWriter getWriter(OutputStream outputStream) throws IOException {
         NBTOutputStream nbtStream = new NBTOutputStream(new GZIPOutputStream(outputStream));
         return new SchematicWriter(nbtStream);
      }

      @Override
      public boolean isFormat(File file) {
         DataInputStream str = null;

         boolean e;
         try {
            str = new DataInputStream(new GZIPInputStream(new FileInputStream(file)));
            if ((str.readByte() & 255) == 10) {
               byte[] nameBytes = new byte[str.readShort() & '\uffff'];
               str.readFully(nameBytes);
               String name = new String(nameBytes, NBTConstants.CHARSET);
               return name.equals("Schematic");
            }

            e = false;
         } catch (IOException var16) {
            return false;
         } finally {
            if (str != null) {
               try {
                  str.close();
               } catch (IOException var15) {
               }
            }
         }

         return e;
      }
   };

   private static final Map<String, ClipboardFormat> aliasMap = new HashMap<>();
   private final String[] aliases;

   private ClipboardFormat(String... aliases) {
      this.aliases = aliases;
   }

   public Set<String> getAliases() {
      return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(this.aliases)));
   }

   public abstract ClipboardReader getReader(InputStream var1) throws IOException;

   public abstract ClipboardWriter getWriter(OutputStream var1) throws IOException;

   public abstract boolean isFormat(File var1);

   @Nullable
   public static ClipboardFormat findByAlias(String alias) {
      Preconditions.checkNotNull(alias);
      return aliasMap.get(alias.toLowerCase().trim());
   }

   @Nullable
   public static ClipboardFormat findByFile(File file) {
      Preconditions.checkNotNull(file);

      for (ClipboardFormat format : EnumSet.allOf(ClipboardFormat.class)) {
         if (format.isFormat(file)) {
            return format;
         }
      }

      return null;
   }

   static {
      for (ClipboardFormat format : EnumSet.allOf(ClipboardFormat.class)) {
         for (String key : format.aliases) {
            aliasMap.put(key, format);
         }
      }
   }
}
