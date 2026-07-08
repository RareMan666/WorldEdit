package com.sk89q.worldedit.internal.gson.internal;

import com.sk89q.worldedit.internal.gson.stream.JsonReader;
import java.io.IOException;

public abstract class JsonReaderInternalAccess {
   public static JsonReaderInternalAccess INSTANCE;

   public abstract void promoteNameToValue(JsonReader var1) throws IOException;
}
