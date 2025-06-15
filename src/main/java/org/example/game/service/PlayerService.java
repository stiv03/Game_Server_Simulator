package org.example.game.service;

import org.example.game.enums.Direction;
import org.example.game.model.Player;
import org.example.persistence.entity.GameSession;
import org.example.persistence.entity.Users;

import java.util.Collection;
import java.util.UUID;

public interface PlayerService extends EntityService {

    Player getPlayer(UUID id);

    Player registerPlayer(Users user, GameSession session);

    void move(UUID playerId, Direction direction) throws InterruptedException;

    void attack(UUID attackerId, UUID targetId) throws InterruptedException;

    void defend(UUID playerId);

    void takeDamage(UUID playerId, int amount);

    void gainXp(UUID playerId, int amount);

    void onDeath(UUID playerId);

    Collection<Player> getAllPlayers();
}
