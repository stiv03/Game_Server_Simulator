package org.example.game.model;

import lombok.Getter;
import lombok.Setter;
import org.example.game.enums.Effect;

@Getter
@Setter
public class Item {

    private Position position;
    private Effect effect;
    private boolean consumed;

    private final Object lock = new Object();

    public Item(Position position, Effect effect) {
        this.position = position;
        this.effect = effect;
    }

    public boolean consume() {
        synchronized (lock) {
            if (consumed) return false;
            consumed = true;
            return true;
        }
    }

    public boolean isConsumed() {
        synchronized (lock) {
            return consumed;
        }
    }
}
