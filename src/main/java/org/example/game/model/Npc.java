package org.example.game.model;


import lombok.Getter;
import lombok.Setter;
import org.example.game.enums.Direction;
import org.example.game.core.Entity;
import org.example.game.enums.NpcType;
import org.example.game.logic.Attack;
import org.example.game.logic.Movement;

import java.util.UUID;

@Getter
@Setter
public class Npc implements Entity {

    private final String id = UUID.randomUUID().toString();
    private final String name;

    private final NpcType type;

    private Position position;
    private int health = 50;
    @Getter
    private boolean defending = false;

    public Npc(NpcType type, Position position) {
        this.type = type;
        this.position = position;
        name = type.toString().toLowerCase() + id;
    }

    public void move(Direction direction) {
        // logika algoritam movement?
        this.position = Movement.getNewPosition(this.position, direction);
    }

    public void attack(Entity target) {
        Attack.performAttack(this, target);
    }

    public void defend() {
        defending = true;
    }

    public void takeDamage(int amount) {
        if (!defending) {
            health -= amount;
        } else {
            health -= amount / 2;
        }
        if (health < 0) health = 0;
    }

    public boolean isAlive() {
        return health > 0;
    }
}
