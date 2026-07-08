package com.sk89q.worldedit.internal.gson;

import com.sk89q.worldedit.internal.gson.reflect.TypeToken;

public interface TypeAdapterFactory {
   <T> TypeAdapter<T> create(Gson var1, TypeToken<T> var2);
}
