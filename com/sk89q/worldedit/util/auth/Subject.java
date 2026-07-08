package com.sk89q.worldedit.util.auth;

public interface Subject {
   String[] getGroups();

   void checkPermission(String var1) throws AuthorizationException;

   boolean hasPermission(String var1);
}
