package org.example.game.model;

import lombok.Getter;
import lombok.Setter;
import org.example.config.lock.LockManager;
import org.example.game.enums.Effect;

import java.util.UUID;

@Getter
@Setter
public class Item {

    private final UUID id = UUID.randomUUID();

    private Position position;
    private Effect effect;
    private boolean consumed;

    public Item(Position position, Effect effect) {
        this.position = position;
        this.effect = effect;
    }

    public boolean consume() {
        synchronized (getLock()) {
            if (consumed) return false;
            consumed = true;
            return true;
        }
    }

    public boolean isConsumed() {
        synchronized (getLock()) {
            return consumed;
        }
    }

    public Object getLock() {
        return LockManager.getLock(getId());
    }
}
