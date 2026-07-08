package com.sk89q.worldedit.session;

import com.sk89q.worldedit.util.auth.Subject;

public interface SessionOwner extends Subject {
   SessionKey getSessionKey();
}
