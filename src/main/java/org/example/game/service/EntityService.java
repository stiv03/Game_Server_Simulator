package org.example.game.service;

import org.example.game.enums.Direction;

import java.util.UUID;

public interface EntityService {

    void move(UUID entityId, Direction direction) throws InterruptedException;

    void attack(UUID attackerId, UUID targetId) throws InterruptedException;

    void defend(UUID entityId);

    void takeDamage(UUID entityId, int amount);

    void onDeath(UUID entityId);

}