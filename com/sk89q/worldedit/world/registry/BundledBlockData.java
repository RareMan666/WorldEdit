package com.sk89q.worldedit.world.registry;

import com.google.common.io.Resources;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BlockMaterial;
import com.sk89q.worldedit.internal.gson.Gson;
import com.sk89q.worldedit.internal.gson.GsonBuilder;
import com.sk89q.worldedit.internal.gson.reflect.TypeToken;
import com.sk89q.worldedit.util.gson.VectorAdapter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

public class BundledBlockData {
   private static final Logger log = Logger.getLogger(BundledBlockData.class.getCanonicalName());
   private static final BundledBlockData INSTANCE = new BundledBlockData();
   private final Map<String, BundledBlockData.BlockEntry> idMap = new HashMap<>();
   private final Map<Integer, BundledBlockData.BlockEntry> legacyMap = new HashMap<>();

   private BundledBlockData() {
      try {
         this.loadFromResource();
      } catch (IOException var2) {
         log.log(Level.WARNING, "Failed to load the built-in block registry", (Throwable)var2);
      }
   }

   private void loadFromResource() throws IOException {
      GsonBuilder gsonBuilder = new GsonBuilder();
      gsonBuilder.registerTypeAdapter(Vector.class, new VectorAdapter());
      Gson gson = gsonBuilder.create();
      URL url = BundledBlockData.class.getResource("blocks.json");
      if (url == null) {
         throw new IOException("Could not find blocks.json");
      } else {
         String data = Resources.toString(url, Charset.defaultCharset());

         for (BundledBlockData.BlockEntry entry : (List<BundledBlockData.BlockEntry>)gson.fromJson(data, (new TypeToken<List<BundledBlockData.BlockEntry>>() {}).getType())) {
            entry.postDeserialization();
            this.idMap.put(entry.id, entry);
            this.legacyMap.put(entry.legacyId, entry);
         }
      }
   }

   @Nullable
   private BundledBlockData.BlockEntry findById(String id) {
      return this.idMap.get(id);
   }

   @Nullable
   private BundledBlockData.BlockEntry findById(int id) {
      return this.legacyMap.get(id);
   }

   @Nullable
   public Integer toLegacyId(String id) {
      BundledBlockData.BlockEntry entry = this.findById(id);
      return entry != null ? entry.legacyId : null;
   }

   @Nullable
   public BlockMaterial getMaterialById(int id) {
      BundledBlockData.BlockEntry entry = this.findById(id);
      return entry != null ? entry.material : null;
   }

   @Nullable
   public Map<String, ? extends State> getStatesById(int id) {
      BundledBlockData.BlockEntry entry = this.findById(id);
      return entry != null ? entry.states : null;
   }

   public static BundledBlockData getInstance() {
      return INSTANCE;
   }

   private static class BlockEntry {
      private int legacyId;
      private String id;
      private String unlocalizedName;
      private List<String> aliases;
      private Map<String, SimpleState> states = new HashMap<>();
      private SimpleBlockMaterial material = new SimpleBlockMaterial();

      void postDeserialization() {
         for (SimpleState state : this.states.values()) {
            state.postDeserialization();
         }
      }
   }
}
