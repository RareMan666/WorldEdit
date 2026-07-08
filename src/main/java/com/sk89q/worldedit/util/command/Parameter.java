package com.sk89q.worldedit.util.command;

public interface Parameter {
   String getName();

   Character getFlag();

   boolean isValueFlag();

   boolean isOptional();

   String[] getDefaultValue();
}
