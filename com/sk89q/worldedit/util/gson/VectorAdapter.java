package com.sk89q.worldedit.util.gson;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.internal.gson.JsonArray;
import com.sk89q.worldedit.internal.gson.JsonDeserializationContext;
import com.sk89q.worldedit.internal.gson.JsonDeserializer;
import com.sk89q.worldedit.internal.gson.JsonElement;
import com.sk89q.worldedit.internal.gson.JsonParseException;
import java.lang.reflect.Type;

public class VectorAdapter implements JsonDeserializer<Vector> {
   public Vector deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      JsonArray jsonArray = json.getAsJsonArray();
      if (jsonArray.size() != 3) {
         throw new JsonParseException("Expected array of 3 length for Vector");
      } else {
         double x = jsonArray.get(0).getAsDouble();
         double y = jsonArray.get(1).getAsDouble();
         double z = jsonArray.get(2).getAsDouble();
         return new Vector(x, y, z);
      }
   }
}
