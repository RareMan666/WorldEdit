package com.sk89q.worldedit.util.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public final class EvenMoreExecutors {
   private EvenMoreExecutors() {
   }

   public static ExecutorService newBoundedCachedThreadPool(int minThreads, int maxThreads, int queueSize) {
      ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(minThreads, maxThreads, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize));
      threadPoolExecutor.allowCoreThreadTimeOut(true);
      return threadPoolExecutor;
   }
}
