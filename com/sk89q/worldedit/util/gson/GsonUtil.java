package com.sk89q.worldedit.util.gson;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.internal.gson.Gson;
import com.sk89q.worldedit.internal.gson.GsonBuilder;

public final class GsonUtil {
   private static final Gson gson = new Gson();

   private GsonUtil() {
   }

   public static GsonBuilder createBuilder() {
      GsonBuilder gsonBuilder = new GsonBuilder();
      gsonBuilder.registerTypeAdapter(Vector.class, new VectorAdapter());
      return gsonBuilder;
   }

   public static String stringValue(String s) {
      return gson.toJson(s);
   }
}
