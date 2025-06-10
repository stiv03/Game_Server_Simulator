package org.example.game.model;


import lombok.Getter;
import lombok.Setter;
import org.example.game.logic.Attack;
import org.example.game.logic.Movement;
import org.example.persistence.entity.Users;
import org.example.game.enums.Direction;
import org.example.game.core.Entity;
import org.example.persistence.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;


@Getter
@Setter
public class Player implements Entity {

    private static final int MAH_HEALTH = 100;
    private static final int XP_NEEDED_TO_LEVEL_UP = 100;

    private Users user;
    private Position position;
    private int health = MAH_HEALTH;

    private int xp;
    private boolean defending;
    private boolean doubleDamage;
    private boolean invincible;
    private boolean speedBoost;

    private long doubleDamageEndTime = 0;
    private long invincibilityEndTime = 0;
    private long speedBoostEndTime = 0;

    private final UsersService userService;

    @Autowired
    public Player(UsersService usersService) {
        this.userService = usersService;
    }

    public String getId() {
        return user.getId().toString();
    }

    public String getName() {
        return user.getUsername();
    }

    public void move(Direction direction) {
        this.position = Movement.getNewPosition(this.position, direction);

        if (speedBoost) {
            this.position = Movement.getNewPosition(this.position, direction);
        }
    }

    public void attack(Entity target) {
        Attack.performAttack(this, target);
    }

    public void defend() {
        defending = true;
    }

    public synchronized void takeDamage(int amount) {

        // add logic
    }

    public synchronized boolean isAlive() {
        return health > 0;
    }

    public void gainXp(int amount) {
        xp += amount;
        if (xp >= XP_NEEDED_TO_LEVEL_UP) {
            levelUp();
        }
    }

    private void levelUp() {
        userService.levelUpUser(user.getId());
        xp = 0;
    }

    private void refreshEffects() {
        long now = System.currentTimeMillis();

        if (doubleDamage && now > doubleDamageEndTime) {
            doubleDamage = false;
        }

        if (invincible && now > invincibilityEndTime) {
            invincible = false;
        }

        if (speedBoost && now > speedBoostEndTime) {
            speedBoost = false;
        }
    }
}
