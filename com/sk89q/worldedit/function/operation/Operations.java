package com.sk89q.worldedit.function.operation;

import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEditException;

public final class Operations {
   private Operations() {
   }

   public static void complete(Operation op) throws WorldEditException {
      while (op != null) {
         op = op.resume(new RunContext());
      }
   }

   public static void completeLegacy(Operation op) throws MaxChangedBlocksException {
      while (op != null) {
         try {
            op = op.resume(new RunContext());
         } catch (MaxChangedBlocksException var2) {
            throw var2;
         } catch (WorldEditException var3) {
            throw new RuntimeException(var3);
         }
      }
   }

   public static void completeBlindly(Operation op) {
      while (op != null) {
         try {
            op = op.resume(new RunContext());
         } catch (WorldEditException var2) {
            throw new RuntimeException(var2);
         }
      }
   }
}
