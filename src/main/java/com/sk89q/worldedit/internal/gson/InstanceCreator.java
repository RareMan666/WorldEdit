package com.sk89q.worldedit.internal.gson;

import java.lang.reflect.Type;

public interface InstanceCreator<T> {
   T createInstance(Type var1);
}
