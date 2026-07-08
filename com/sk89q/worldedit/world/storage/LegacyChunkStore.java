package com.sk89q.worldedit.world.storage;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.World;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

public abstract class LegacyChunkStore extends ChunkStore {
   public static String getFilename(Vector2D position, String separator) {
      int x = position.getBlockX();
      int z = position.getBlockZ();
      String folder1 = Integer.toString(divisorMod(x, 64), 36);
      String folder2 = Integer.toString(divisorMod(z, 64), 36);
      String filename = "c." + Integer.toString(x, 36) + "." + Integer.toString(z, 36) + ".dat";
      return folder1 + separator + folder2 + separator + filename;
   }

   public static String getFilename(Vector2D position) {
      return getFilename(position, File.separator);
   }

   @Override
   public CompoundTag getChunkTag(Vector2D position, World world) throws DataException, IOException {
      int x = position.getBlockX();
      int z = position.getBlockZ();
      String folder1 = Integer.toString(divisorMod(x, 64), 36);
      String folder2 = Integer.toString(divisorMod(z, 64), 36);
      String filename = "c." + Integer.toString(x, 36) + "." + Integer.toString(z, 36) + ".dat";
      InputStream stream = this.getInputStream(folder1, folder2, filename);
      NBTInputStream nbt = new NBTInputStream(new GZIPInputStream(stream));

      CompoundTag var18;
      try {
         Tag tag = nbt.readNamedTag().getTag();
         if (!(tag instanceof CompoundTag)) {
            throw new ChunkStoreException("CompoundTag expected for chunk; got " + tag.getClass().getName());
         }

         Map<String, Tag> children = ((CompoundTag)tag).getValue();
         CompoundTag rootTag = null;

         for (Entry<String, Tag> entry : children.entrySet()) {
            if (entry.getKey().equals("Level")) {
               if (!(entry.getValue() instanceof CompoundTag)) {
                  throw new ChunkStoreException("CompoundTag expected for 'Level'; got " + entry.getValue().getClass().getName());
               }

               rootTag = (CompoundTag)entry.getValue();
               break;
            }
         }

         if (rootTag == null) {
            throw new ChunkStoreException("Missing root 'Level' tag");
         }

         var18 = rootTag;
      } finally {
         nbt.close();
      }

      return var18;
   }

   private static int divisorMod(int a, int n) {
      return (int)(a - n * Math.floor(Math.floor(a) / n));
   }

   protected abstract InputStream getInputStream(String var1, String var2, String var3) throws IOException, DataException;
}
