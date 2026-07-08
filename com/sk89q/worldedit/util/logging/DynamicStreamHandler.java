package com.sk89q.worldedit.util.logging;

import java.io.UnsupportedEncodingException;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import javax.annotation.Nullable;

public class DynamicStreamHandler extends StreamHandler {
   @Nullable
   private StreamHandler handler;
   @Nullable
   private Formatter formatter;
   @Nullable
   private Filter filter;
   @Nullable
   private String encoding;
   private Level level = Level.ALL;

   @Nullable
   public synchronized StreamHandler getHandler() {
      return this.handler;
   }

   public synchronized void setHandler(@Nullable StreamHandler handler) {
      if (this.handler != null) {
         this.handler.close();
      }

      this.handler = handler;
      if (handler != null) {
         handler.setFormatter(this.formatter);
         handler.setFilter(this.filter);

         try {
            handler.setEncoding(this.encoding);
         } catch (UnsupportedEncodingException var3) {
         }

         handler.setLevel(this.level);
      }
   }

   @Override
   public synchronized void publish(LogRecord record) {
      if (this.handler != null) {
         this.handler.publish(record);
      }
   }

   @Override
   public synchronized void close() throws SecurityException {
      if (this.handler != null) {
         this.handler.close();
      }
   }

   @Override
   public void setEncoding(@Nullable String encoding) throws SecurityException, UnsupportedEncodingException {
      StreamHandler handler = this.handler;
      this.encoding = encoding;
      if (handler != null) {
         handler.setEncoding(encoding);
      }
   }

   @Override
   public boolean isLoggable(LogRecord record) {
      StreamHandler handler = this.handler;
      return handler != null && handler.isLoggable(record);
   }

   @Override
   public synchronized void flush() {
      StreamHandler handler = this.handler;
      if (handler != null) {
         handler.flush();
      }
   }

   @Override
   public void setFormatter(@Nullable Formatter newFormatter) throws SecurityException {
      StreamHandler handler = this.handler;
      this.formatter = newFormatter;
      if (handler != null) {
         handler.setFormatter(newFormatter);
      }
   }

   @Override
   public Formatter getFormatter() {
      StreamHandler handler = this.handler;
      Formatter formatter = this.formatter;
      if (handler != null) {
         return handler.getFormatter();
      } else {
         return (Formatter)(formatter != null ? formatter : new SimpleFormatter());
      }
   }

   @Override
   public String getEncoding() {
      StreamHandler handler = this.handler;
      String encoding = this.encoding;
      return handler != null ? handler.getEncoding() : encoding;
   }

   @Override
   public void setFilter(@Nullable Filter newFilter) throws SecurityException {
      StreamHandler handler = this.handler;
      this.filter = newFilter;
      if (handler != null) {
         handler.setFilter(newFilter);
      }
   }

   @Override
   public Filter getFilter() {
      StreamHandler handler = this.handler;
      Filter filter = this.filter;
      return handler != null ? handler.getFilter() : filter;
   }

   @Override
   public synchronized void setLevel(Level newLevel) throws SecurityException {
      if (this.handler != null) {
         this.handler.setLevel(newLevel);
      }

      this.level = newLevel;
   }

   @Override
   public synchronized Level getLevel() {
      return this.handler != null ? this.handler.getLevel() : this.level;
   }
}
