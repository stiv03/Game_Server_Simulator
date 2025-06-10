package org.example.game.model;


import lombok.Getter;
import lombok.Setter;
import org.example.game.logic.Attack;
import org.example.game.logic.Movement;
import org.example.persistence.entity.Users;
import org.example.game.enums.Direction;
import org.example.game.core.Entity;


@Getter
@Setter
public class Player implements Entity {

    private Users user;
    private Position position;
    private int health = 100;
    @Getter
    private boolean defending = false;

    public String getId() {
        return user.getId().toString();
    }

    public String getName() {
        return user.getUsername();
    }

    public void move(Direction direction) {
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
