package com.sk89q.worldedit.extension.platform;

import com.sk89q.worldedit.internal.cui.CUIEvent;
import com.sk89q.worldedit.session.SessionOwner;
import com.sk89q.worldedit.util.Identifiable;
import com.sk89q.worldedit.util.auth.Subject;
import java.io.File;

public interface Actor extends Identifiable, SessionOwner, Subject {
   String getName();

   void printRaw(String var1);

   void printDebug(String var1);

   void print(String var1);

   void printError(String var1);

   boolean canDestroyBedrock();

   boolean isPlayer();

   File openFileOpenDialog(String[] var1);

   File openFileSaveDialog(String[] var1);

   void dispatchCUIEvent(CUIEvent var1);
}
