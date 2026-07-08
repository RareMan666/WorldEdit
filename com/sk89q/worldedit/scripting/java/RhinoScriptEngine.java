package com.sk89q.worldedit.scripting.java;

import com.sk89q.worldedit.scripting.RhinoContextFactory;
import java.io.IOException;
import java.io.Reader;
import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class RhinoScriptEngine extends AbstractScriptEngine {
   private ScriptEngineFactory factory;
   private Context cx;

   public RhinoScriptEngine() {
      RhinoContextFactory factory = new RhinoContextFactory(3000);
      factory.enterContext();
   }

   @Override
   public Bindings createBindings() {
      return new SimpleBindings();
   }

   @Override
   public Object eval(String script, ScriptContext context) throws ScriptException {
      Scriptable scope = this.setupScope(this.cx, context);
      String filename;
      filename = (filename = (String)this.get("javax.script.filename")) == null ? "<unknown>" : filename;

      Object e;
      try {
         e = this.cx.evaluateString(scope, script, filename, 1, null);
      } catch (RhinoException var12) {
         int line;
         line = (line = var12.lineNumber()) == 0 ? -1 : line;
         String msg;
         if (var12 instanceof JavaScriptException) {
            msg = String.valueOf(((JavaScriptException)var12).getValue());
         } else {
            msg = var12.getMessage();
         }

         ScriptException scriptException = new ScriptException(msg, var12.sourceName(), line);
         scriptException.initCause(var12);
         throw scriptException;
      } finally {
         Context.exit();
      }

      return e;
   }

   @Override
   public Object eval(Reader reader, ScriptContext context) throws ScriptException {
      Scriptable scope = this.setupScope(this.cx, context);
      String filename;
      filename = (filename = (String)this.get("javax.script.filename")) == null ? "<unknown>" : filename;

      Object e;
      try {
         e = this.cx.evaluateReader(scope, reader, filename, 1, null);
      } catch (RhinoException var13) {
         int line;
         line = (line = var13.lineNumber()) == 0 ? -1 : line;
         String msg;
         if (var13 instanceof JavaScriptException) {
            msg = String.valueOf(((JavaScriptException)var13).getValue());
         } else {
            msg = var13.getMessage();
         }

         ScriptException scriptException = new ScriptException(msg, var13.sourceName(), line);
         scriptException.initCause(var13);
         throw scriptException;
      } catch (IOException var14) {
         throw new ScriptException(var14);
      } finally {
         Context.exit();
      }

      return e;
   }

   @Override
   public ScriptEngineFactory getFactory() {
      return (ScriptEngineFactory)(this.factory != null ? this.factory : new RhinoScriptEngineFactory());
   }

   private Scriptable setupScope(Context cx, ScriptContext context) {
      ScriptableObject scriptable = new ImporterTopLevel(cx);
      return cx.initStandardObjects(scriptable);
   }
}
