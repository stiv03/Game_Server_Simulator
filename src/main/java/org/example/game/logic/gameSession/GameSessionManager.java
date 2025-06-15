package org.example.game.logic.gameSession;

import org.example.persistence.entity.GameSession;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameSessionManager {

    private final Map<UUID, GameSessionContext> contexts = new ConcurrentHashMap<>();
    private final Map<UUID, Thread> threads = new ConcurrentHashMap<>();

    @Autowired
    private ObjectProvider<GameSessionContext> contextProvider;

    public void startSession(GameSession session) {
        GameSessionContext context = contextProvider.getObject();
        context.loadSession(session.getId());
        context.startSession();

        Thread thread = new Thread(context);
        thread.start();

        contexts.put(session.getId(), context);
        threads.put(session.getId(), thread);
    }

    public void stopSession(UUID sessionId) {
        GameSessionContext context = contexts.get(sessionId);
        Thread thread = threads.get(sessionId);
        if (context != null && thread != null) {
            context.stop();
            thread.interrupt();
        }
        contexts.remove(sessionId);
        threads.remove(sessionId);
    }

    public GameSessionContext getContext(UUID sessionId) {
        return contexts.get(sessionId);
    }

    public GameSessionContext getOrLoadContext(UUID sessionId) {
        return contexts.computeIfAbsent(sessionId, id -> {
            GameSessionContext context = contextProvider.getObject();
            context.loadSession(id);
            return context;
        });
    }
}
