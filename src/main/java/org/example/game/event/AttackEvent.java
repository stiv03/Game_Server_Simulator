package org.example.game.event;

import lombok.AllArgsConstructor;
import org.example.game.model.Entity;
import org.example.game.logic.Attack;
import org.example.persistence.entity.GameSession;


@AllArgsConstructor
public class AttackEvent implements GameEvent {
    private final Entity attacker;
    private final Entity target;

    @Override
    public void execute(GameSession session) throws InterruptedException {
        Attack.hitTarget(attacker, target);
    }
}
