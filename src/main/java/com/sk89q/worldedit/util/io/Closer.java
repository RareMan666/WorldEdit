package com.sk89q.worldedit.util.io;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

public final class Closer implements Closeable {
   private static final Logger logger = Logger.getLogger(Closer.class.getCanonicalName());
   private static final Closer.Suppressor SUPPRESSOR = (Closer.Suppressor)(Closer.SuppressingSuppressor.isAvailable()
      ? Closer.SuppressingSuppressor.INSTANCE
      : Closer.LoggingSuppressor.INSTANCE);
   @VisibleForTesting
   final Closer.Suppressor suppressor;
   private final Deque<Closeable> stack = new ArrayDeque<>(4);
   private final Deque<ZipFile> zipStack = new ArrayDeque<>(4);
   private Throwable thrown;

   public static Closer create() {
      return new Closer(SUPPRESSOR);
   }

   @VisibleForTesting
   Closer(Closer.Suppressor suppressor) {
      this.suppressor = (Closer.Suppressor)Preconditions.checkNotNull(suppressor);
   }

   public <C extends Closeable> C register(C closeable) {
      this.stack.push(closeable);
      return closeable;
   }

   public <Z extends ZipFile> Z register(Z zipFile) {
      this.zipStack.push(zipFile);
      return zipFile;
   }

   public RuntimeException rethrow(Throwable e) throws IOException {
      this.thrown = e;
      Throwables.propagateIfPossible(e, IOException.class);
      throw Throwables.propagate(e);
   }

   public <X extends Exception> RuntimeException rethrow(Throwable e, Class<X> declaredType) throws IOException, X {
      this.thrown = e;
      Throwables.propagateIfPossible(e, IOException.class);
      Throwables.propagateIfPossible(e, declaredType);
      throw Throwables.propagate(e);
   }

   public <X1 extends Exception, X2 extends Exception> RuntimeException rethrow(Throwable e, Class<X1> declaredType1, Class<X2> declaredType2) throws IOException, X1, X2 {
      this.thrown = e;
      Throwables.propagateIfPossible(e, IOException.class);
      Throwables.propagateIfPossible(e, declaredType1, declaredType2);
      throw Throwables.propagate(e);
   }

   @Override
   public void close() throws IOException {
      Throwable throwable = this.thrown;

      while (!this.stack.isEmpty()) {
         Closeable closeable = this.stack.pop();

         try {
            closeable.close();
         } catch (Throwable var5) {
            if (throwable == null) {
               throwable = var5;
            } else {
               this.suppressor.suppress(closeable, throwable, var5);
            }
         }
      }

      while (!this.zipStack.isEmpty()) {
         ZipFile zipFile = this.zipStack.pop();

         try {
            zipFile.close();
         } catch (Throwable var4) {
            if (throwable == null) {
               throwable = var4;
            } else {
               this.suppressor.suppress(zipFile, throwable, var4);
            }
         }
      }

      if (this.thrown == null && throwable != null) {
         Throwables.propagateIfPossible(throwable, IOException.class);
         throw new AssertionError(throwable);
      }
   }

   @VisibleForTesting
   static final class LoggingSuppressor implements Closer.Suppressor {
      static final Closer.LoggingSuppressor INSTANCE = new Closer.LoggingSuppressor();

      @Override
      public void suppress(Object closeable, Throwable thrown, Throwable suppressed) {
         Closer.logger.log(Level.WARNING, "Suppressing exception thrown when closing " + closeable, suppressed);
      }
   }

   @VisibleForTesting
   static final class SuppressingSuppressor implements Closer.Suppressor {
      static final Closer.SuppressingSuppressor INSTANCE = new Closer.SuppressingSuppressor();
      static final Method addSuppressed = getAddSuppressed();

      static boolean isAvailable() {
         return addSuppressed != null;
      }

      private static Method getAddSuppressed() {
         try {
            return Throwable.class.getMethod("addSuppressed", Throwable.class);
         } catch (Throwable var1) {
            return null;
         }
      }

      @Override
      public void suppress(Object closeable, Throwable thrown, Throwable suppressed) {
         if (thrown != suppressed) {
            try {
               addSuppressed.invoke(thrown, suppressed);
            } catch (Throwable var5) {
               Closer.LoggingSuppressor.INSTANCE.suppress(closeable, thrown, suppressed);
            }
         }
      }
   }

   @VisibleForTesting
   interface Suppressor {
      void suppress(Object var1, Throwable var2, Throwable var3);
   }
}
