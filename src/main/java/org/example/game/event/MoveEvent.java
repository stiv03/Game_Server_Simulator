package org.example.game.event;

import lombok.AllArgsConstructor;
import org.example.game.model.Entity;
import org.example.game.enums.Direction;
import org.example.game.logic.Move;
import org.example.game.model.Position;
import org.example.persistence.entity.GameSession;

@AllArgsConstructor
public class MoveEvent implements GameEvent{
    private final Entity entity;
    private final Direction direction;

    @Override
    public void execute(GameSession session) {
        Position newPosition = Move.getNewPosition(entity.getPosition(), direction);
        entity.setPosition(newPosition);
    }
}
