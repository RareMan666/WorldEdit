package com.sk89q.worldedit.session;

import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.event.platform.ConfigurationLoadEvent;
import com.sk89q.worldedit.session.storage.JsonFileSessionStore;
import com.sk89q.worldedit.session.storage.SessionStore;
import com.sk89q.worldedit.session.storage.VoidStore;
import com.sk89q.worldedit.util.concurrency.EvenMoreExecutors;
import com.sk89q.worldedit.util.eventbus.Subscribe;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;

public class SessionManager {
   public static int EXPIRATION_GRACE = 600000;
   private static final int FLUSH_PERIOD = 30000;
   private static final ListeningExecutorService executorService = MoreExecutors.listeningDecorator(EvenMoreExecutors.newBoundedCachedThreadPool(0, 1, 5));
   private static final Logger log = Logger.getLogger(SessionManager.class.getCanonicalName());
   private final Timer timer = new Timer();
   private final WorldEdit worldEdit;
   private final Map<UUID, SessionManager.SessionHolder> sessions = new HashMap<>();
   private SessionStore store = new VoidStore();

   public SessionManager(WorldEdit worldEdit) {
      Preconditions.checkNotNull(worldEdit);
      this.worldEdit = worldEdit;
      worldEdit.getEventBus().register(this);
      this.timer.schedule(new SessionManager.SessionTracker(), 30000L, 30000L);
   }

   public synchronized boolean contains(SessionOwner owner) {
      Preconditions.checkNotNull(owner);
      return this.sessions.containsKey(this.getKey(owner));
   }

   @Nullable
   public synchronized LocalSession findByName(String name) {
      Preconditions.checkNotNull(name);

      for (SessionManager.SessionHolder holder : this.sessions.values()) {
         String test = holder.key.getName();
         if (test != null && name.equals(test)) {
            return holder.session;
         }
      }

      return null;
   }

   @Nullable
   public synchronized LocalSession getIfPresent(SessionOwner owner) {
      Preconditions.checkNotNull(owner);
      SessionManager.SessionHolder stored = this.sessions.get(this.getKey(owner));
      return stored != null ? stored.session : null;
   }

   public synchronized LocalSession get(SessionOwner owner) {
      Preconditions.checkNotNull(owner);
      LocalSession session = this.getIfPresent(owner);
      LocalConfiguration config = this.worldEdit.getConfiguration();
      SessionKey sessionKey = owner.getSessionKey();
      if (session == null) {
         try {
            session = this.store.load(this.getKey(sessionKey));
            session.postLoad();
         } catch (IOException var7) {
            log.log(Level.WARNING, "Failed to load saved session", (Throwable)var7);
            session = new LocalSession();
         }

         session.setConfiguration(config);
         session.setBlockChangeLimit(config.defaultChangeLimit);
         if (sessionKey.isActive()) {
            this.sessions.put(this.getKey(owner), new SessionManager.SessionHolder(sessionKey, session));
         }
      }

      int currentChangeLimit = session.getBlockChangeLimit();
      if (!owner.hasPermission("worldedit.limit.unrestricted") && config.maxChangeLimit > -1) {
         if (config.defaultChangeLimit < 0) {
            if (currentChangeLimit < 0 || currentChangeLimit > config.maxChangeLimit) {
               session.setBlockChangeLimit(config.maxChangeLimit);
            }
         } else {
            int maxChangeLimit = config.maxChangeLimit;
            if (currentChangeLimit == -1 || currentChangeLimit > maxChangeLimit) {
               session.setBlockChangeLimit(maxChangeLimit);
            }
         }
      }

      session.setUseInventory(
         config.useInventory
            && (
               !config.useInventoryOverride
                  || !owner.hasPermission("worldedit.inventory.unrestricted")
                     && (!config.useInventoryCreativeOverride || owner instanceof Player && !((Player)owner).hasCreativeMode())
            )
      );
      return session;
   }

   private ListenableFuture<?> commit(final Map<SessionKey, LocalSession> sessions) {
      Preconditions.checkNotNull(sessions);
      return sessions.isEmpty() ? Futures.immediateFuture(sessions) : executorService.submit(new Callable<Object>() {
         @Override
         public Object call() throws Exception {
            Exception exception = null;

            for (Entry<SessionKey, LocalSession> entry : sessions.entrySet()) {
               SessionKey key = entry.getKey();
               if (key.isPersistent()) {
                  try {
                     SessionManager.this.store.save(SessionManager.this.getKey(key), entry.getValue());
                  } catch (IOException var6) {
                     SessionManager.log.log(Level.WARNING, "Failed to write session for UUID " + SessionManager.this.getKey(key), (Throwable)var6);
                     exception = var6;
                  }
               }
            }

            if (exception != null) {
               throw exception;
            } else {
               return sessions;
            }
         }
      });
   }

   protected UUID getKey(SessionOwner owner) {
      return this.getKey(owner.getSessionKey());
   }

   protected UUID getKey(SessionKey key) {
      String forcedKey = System.getProperty("worldedit.session.uuidOverride");
      return forcedKey != null ? UUID.fromString(forcedKey) : key.getUniqueId();
   }

   public synchronized void remove(SessionOwner owner) {
      Preconditions.checkNotNull(owner);
      this.sessions.remove(this.getKey(owner));
   }

   public synchronized void clear() {
      this.sessions.clear();
   }

   @Subscribe
   public void onConfigurationLoad(ConfigurationLoadEvent event) {
      LocalConfiguration config = event.getConfiguration();
      File dir = new File(config.getWorkingDirectory(), "sessions");
      this.store = new JsonFileSessionStore(dir);
   }

   private static class SessionHolder {
      private final SessionKey key;
      private final LocalSession session;
      private long lastActive = System.currentTimeMillis();

      private SessionHolder(SessionKey key, LocalSession session) {
         this.key = key;
         this.session = session;
      }
   }

   private class SessionTracker extends TimerTask {
      private SessionTracker() {
      }

      @Override
      public void run() {
         synchronized (SessionManager.this) {
            long now = System.currentTimeMillis();
            Iterator<SessionManager.SessionHolder> it = SessionManager.this.sessions.values().iterator();
            Map<SessionKey, LocalSession> saveQueue = new HashMap<>();

            while (it.hasNext()) {
               SessionManager.SessionHolder stored = it.next();
               if (stored.key.isActive()) {
                  stored.lastActive = now;
                  if (stored.session.compareAndResetDirty()) {
                     saveQueue.put(stored.key, stored.session);
                  }
               } else if (now - stored.lastActive > SessionManager.EXPIRATION_GRACE) {
                  if (stored.session.compareAndResetDirty()) {
                     saveQueue.put(stored.key, stored.session);
                  }

                  it.remove();
               }
            }

            if (!saveQueue.isEmpty()) {
               SessionManager.this.commit(saveQueue);
            }
         }
      }
   }
}
