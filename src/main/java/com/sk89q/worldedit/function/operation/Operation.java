package com.sk89q.worldedit.function.operation;

import com.sk89q.worldedit.WorldEditException;
import java.util.List;

public interface Operation {
   Operation resume(RunContext var1) throws WorldEditException;

   void cancel();

   void addStatusMessages(List<String> var1);
}
