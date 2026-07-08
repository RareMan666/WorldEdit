package com.sk89q.worldedit.internal.gson;

import java.lang.reflect.Type;

public interface JsonSerializer<T> {
   JsonElement serialize(T var1, Type var2, JsonSerializationContext var3);
}
