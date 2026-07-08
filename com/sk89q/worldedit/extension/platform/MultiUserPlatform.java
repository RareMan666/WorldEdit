package com.sk89q.worldedit.extension.platform;

import java.util.Collection;

public interface MultiUserPlatform extends Platform {
   Collection<Actor> getConnectedUsers();
}
