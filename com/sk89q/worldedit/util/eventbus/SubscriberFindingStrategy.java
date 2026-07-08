package com.sk89q.worldedit.util.eventbus;

import com.google.common.collect.Multimap;

interface SubscriberFindingStrategy {
   Multimap<Class<?>, EventHandler> findAllSubscribers(Object var1);
}
