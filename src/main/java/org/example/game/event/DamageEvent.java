package org.example.game.event;

import lombok.AllArgsConstructor;
import org.example.game.model.Entity;
import org.example.game.logic.Damage;
import org.example.persistence.entity.GameSession;

@AllArgsConstructor
public class DamageEvent implements GameEvent {

    private final Entity target;
    private final int amount;

    @Override
    public void execute(GameSession session) {
        Damage.handleDamage(target, amount);
    }
}
