package org.example.game.service;

import org.example.game.enums.Direction;
import org.example.game.model.Entity;
import org.example.game.model.Npc;
import org.example.persistence.entity.GameSession;

import java.util.Collection;
import java.util.UUID;

public interface NpcService extends EntityService{

    Npc spawnNpc(GameSession session);

    void move(UUID npcId, Direction direction) throws InterruptedException;

    void attack(UUID npcId, Entity target) throws InterruptedException;

    void defend(UUID npcId);

    void takeDamage(UUID npcId, int amount);

    void onDeath(UUID npcId);

    Collection<Npc> getAllNpcs();
}
