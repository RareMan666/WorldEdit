package com.sk89q.worldedit.internal.gson.internal.bind;

import com.sk89q.worldedit.internal.gson.Gson;
import com.sk89q.worldedit.internal.gson.JsonSyntaxException;
import com.sk89q.worldedit.internal.gson.TypeAdapter;
import com.sk89q.worldedit.internal.gson.TypeAdapterFactory;
import com.sk89q.worldedit.internal.gson.reflect.TypeToken;
import com.sk89q.worldedit.internal.gson.stream.JsonReader;
import com.sk89q.worldedit.internal.gson.stream.JsonToken;
import com.sk89q.worldedit.internal.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public final class SqlDateTypeAdapter extends TypeAdapter<Date> {
   public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
         return typeToken.getRawType() == Date.class ? (TypeAdapter<T>) new SqlDateTypeAdapter() : null;
      }
   };
   private final DateFormat format = new SimpleDateFormat("MMM d, yyyy");

   public synchronized Date read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
         in.nextNull();
         return null;
      } else {
         try {
            long utilDate = this.format.parse(in.nextString()).getTime();
            return new Date(utilDate);
         } catch (ParseException var5) {
            throw new JsonSyntaxException(var5);
         }
      }
   }

   public synchronized void write(JsonWriter out, Date value) throws IOException {
      out.value(value == null ? null : this.format.format(value));
   }
}
