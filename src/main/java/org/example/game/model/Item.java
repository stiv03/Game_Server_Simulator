package org.example.game.model;

import lombok.Getter;
import lombok.Setter;
import org.example.game.enums.Effect;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
public class Item {

    private Position position;
    private Effect effect;
    private boolean consumed;

    @Autowired
    public Item(Position position, Effect effect) {
        this.position = position;
        this.effect = effect;
    }
}
