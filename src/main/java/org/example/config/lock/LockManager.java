package org.example.config.lock;


import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class LockManager {

    private static final ConcurrentHashMap<UUID, Object> locks = new ConcurrentHashMap<>();

    public static Object getLock(UUID id) {
        return locks.computeIfAbsent(id, key -> new Object());
    }

    public static void removeLock(UUID id) {
        locks.remove(id);
    }
}
