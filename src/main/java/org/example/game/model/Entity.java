package org.example.game.model;

import org.example.game.enums.Direction;


public interface Entity {

    String getId();

    String getName();

    Position getPosition();

    void setPosition(Position position);

    void setHealth(int health);

    void move(Direction direction) throws InterruptedException;

    void attack(Entity target) throws InterruptedException;

    void defend();

    int getHealth();

    void takeDamage(int amount) throws InterruptedException;

    boolean isAlive();

    boolean isDefending();

    void onDeath();
}
