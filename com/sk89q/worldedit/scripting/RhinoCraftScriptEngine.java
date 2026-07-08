package com.sk89q.worldedit.scripting;

import com.sk89q.worldedit.WorldEditException;
import java.util.Map;
import java.util.Map.Entry;
import javax.script.ScriptException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrappedException;

public class RhinoCraftScriptEngine implements CraftScriptEngine {
   private int timeLimit;

   @Override
   public void setTimeLimit(int milliseconds) {
      this.timeLimit = milliseconds;
   }

   @Override
   public int getTimeLimit() {
      return this.timeLimit;
   }

   @Override
   public Object evaluate(String script, String filename, Map<String, Object> args) throws ScriptException, Throwable {
      RhinoContextFactory factory = new RhinoContextFactory(this.timeLimit);
      Context cx = factory.enterContext();
      ScriptableObject scriptable = new ImporterTopLevel(cx);
      Scriptable scope = cx.initStandardObjects(scriptable);

      for (Entry<String, Object> entry : args.entrySet()) {
         ScriptableObject.putProperty(scope, entry.getKey(), Context.javaToJS(entry.getValue(), scope));
      }

      Object var19;
      try {
         var19 = cx.evaluateString(scope, script, filename, 1, null);
      } catch (Error var16) {
         throw new ScriptException(var16.getMessage());
      } catch (RhinoException var17) {
         if (var17 instanceof WrappedException) {
            Throwable cause = var17.getCause();
            if (cause instanceof WorldEditException) {
               throw cause;
            }
         }

         int line;
         line = (line = var17.lineNumber()) == 0 ? -1 : line;
         String msg;
         if (var17 instanceof JavaScriptException) {
            msg = String.valueOf(((JavaScriptException)var17).getValue());
         } else {
            msg = var17.getMessage();
         }

         ScriptException scriptException = new ScriptException(msg, var17.sourceName(), line);
         scriptException.initCause(var17);
         throw scriptException;
      } finally {
         Context.exit();
      }

      return var19;
   }
}
