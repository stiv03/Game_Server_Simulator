package org.example.game.logic.gameSession;

import org.example.game.event.GameEvent;
import org.example.persistence.entity.GameSession;
import org.springframework.beans.factory.annotation.Autowired;

public class GameSessionProcessor extends Thread {
    private final GameSession session;
    private volatile boolean running = true;

    @Autowired
    public GameSessionProcessor(GameSession session) {
        this.session = session;
    }

    @Override
    public void run() {
        while (running && session.getEndTime() == null) {
            try {
                GameEvent event = session.getEventQueue().take();
                event.execute(session);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopProcessing() {
        this.running = false;
    }
}

