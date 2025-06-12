package org.example.game.model;

import java.util.UUID;


public interface Entity {

    UUID getId();

    String getName();

    Position getPosition();

    void setPosition(Position newPosition);

    void setHealth(int newHealth);

    int getHealth();

    boolean isAlive();

    boolean isDefending();

    void disconnect();
}

