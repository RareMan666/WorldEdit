package com.sk89q.worldedit.world.storage;

import com.sk89q.jnbt.CompoundTag;
import com.sk89q.jnbt.NBTInputStream;
import com.sk89q.jnbt.Tag;
import com.sk89q.worldedit.Vector2D;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.World;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Map.Entry;

public abstract class McRegionChunkStore extends ChunkStore {
   protected String curFilename = null;
   protected McRegionReader cachedReader = null;

   public static String getFilename(Vector2D position) {
      int x = position.getBlockX();
      int z = position.getBlockZ();
      return "r." + (x >> 5) + "." + (z >> 5) + ".mca";
   }

   protected McRegionReader getReader(Vector2D pos, String worldname) throws DataException, IOException {
      String filename = getFilename(pos);
      if (this.curFilename != null) {
         if (this.curFilename.equals(filename)) {
            return this.cachedReader;
         }

         try {
            this.cachedReader.close();
         } catch (IOException var5) {
         }
      }

      InputStream stream = this.getInputStream(filename, worldname);
      this.cachedReader = new McRegionReader(stream);
      return this.cachedReader;
   }

   @Override
   public CompoundTag getChunkTag(Vector2D position, World world) throws DataException, IOException {
      McRegionReader reader = this.getReader(position, world.getName());
      InputStream stream = reader.getChunkInputStream(position);
      NBTInputStream nbt = new NBTInputStream(stream);

      CompoundTag var14;
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

         var14 = rootTag;
      } finally {
         nbt.close();
      }

      return var14;
   }

   protected abstract InputStream getInputStream(String var1, String var2) throws IOException, DataException;

   @Override
   public void close() throws IOException {
      if (this.cachedReader != null) {
         this.cachedReader.close();
      }
   }
}
