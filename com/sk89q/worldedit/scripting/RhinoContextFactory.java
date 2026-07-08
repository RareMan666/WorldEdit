package com.sk89q.worldedit.scripting;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;

public class RhinoContextFactory extends ContextFactory {
   protected int timeLimit;

   public RhinoContextFactory(int timeLimit) {
      this.timeLimit = timeLimit;
   }

   protected Context makeContext() {
      RhinoContextFactory.RhinoContext cx = new RhinoContextFactory.RhinoContext(this);
      cx.setInstructionObserverThreshold(10000);
      return cx;
   }

   protected void observeInstructionCount(Context cx, int instructionCount) {
      RhinoContextFactory.RhinoContext mcx = (RhinoContextFactory.RhinoContext)cx;
      long currentTime = System.currentTimeMillis();
      if (currentTime - mcx.startTime > this.timeLimit) {
         throw new Error("Script timed out (" + this.timeLimit + "ms)");
      }
   }

   protected Object doTopCall(Callable callable, Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
      RhinoContextFactory.RhinoContext mcx = (RhinoContextFactory.RhinoContext)cx;
      mcx.startTime = System.currentTimeMillis();
      return super.doTopCall(callable, cx, scope, thisObj, args);
   }

   private static class RhinoContext extends Context {
      long startTime;

      private RhinoContext(ContextFactory factory) {
         super(factory);
      }
   }
}
