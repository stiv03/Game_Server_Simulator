package org.example.game.event;

import org.example.persistence.entity.GameSession;

public interface GameEvent {

    void execute(GameSession session) throws InterruptedException;
}
