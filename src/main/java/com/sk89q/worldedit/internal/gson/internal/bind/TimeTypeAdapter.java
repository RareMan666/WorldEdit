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
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimeTypeAdapter extends TypeAdapter<Time> {
   public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
      @Override
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
         return typeToken.getRawType() == Time.class ? (TypeAdapter<T>) new TimeTypeAdapter() : null;
      }
   };
   private final DateFormat format = new SimpleDateFormat("hh:mm:ss a");

   public synchronized Time read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
         in.nextNull();
         return null;
      } else {
         try {
            Date date = this.format.parse(in.nextString());
            return new Time(date.getTime());
         } catch (ParseException var3) {
            throw new JsonSyntaxException(var3);
         }
      }
   }

   public synchronized void write(JsonWriter out, Time value) throws IOException {
      out.value(value == null ? null : this.format.format(value));
   }
}
