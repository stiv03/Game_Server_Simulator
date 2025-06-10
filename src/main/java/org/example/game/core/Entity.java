package org.example.game.core;

import org.example.game.model.Position;
import org.example.game.enums.Direction;


public interface Entity {

    String getId();

    String getName();

    Position getPosition();

    void setPosition(Position position);

    void move(Direction direction);

    void attack(Entity target);

    void defend();

    int getHealth();

    void takeDamage(int amount);

    boolean isAlive();

    boolean isDefending();
}
