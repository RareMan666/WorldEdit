package com.sk89q.worldedit.history.change;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.history.UndoContext;

public interface Change {
   void undo(UndoContext var1) throws WorldEditException;

   void redo(UndoContext var1) throws WorldEditException;
}
