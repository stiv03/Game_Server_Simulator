package org.example.game.logic;

import org.example.config.messages.LogMessages;
import org.example.exceptions.UnknownEntityType;
import org.example.game.logic.helpers.Distance;
import org.example.game.model.Entity;
import org.example.game.model.Npc;
import org.example.game.model.Player;
import org.example.game.service.NpcService;
import org.example.game.service.PlayerService;

import org.example.game.service.serviceIml.PlayerServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Attack {

    private static final Logger logger = LoggerFactory.getLogger(Attack.class);

    private final static int PLAYER_BASE_DAMAGE = 5;
    private final static int NPC_BASE_DAMAGE = 10;

    private final static int PLAYER_MAH_RANGE = 5;
    private final static int ATTACKER_MAH_RANGE = 10;
    private final static int GUARD_MAH_RANGE = 2;

    private static final double DEFENSE_DAMAGE_REDUCTION_FACTOR = 0.5;

    private final PlayerService playerService;
    private final NpcService npcService;

    @Autowired
    public Attack(PlayerServiceImpl playerService, NpcService npcService) {
        this.playerService = playerService;
        this.npcService = npcService;
    }


    public void hitTarget(Entity attacker, Entity target) {

        if (target == null) {
            logger.warn(LogMessages.MAYBE_LEFT_SESSION);
            return;
        }

        synchronized (target.getLock()) {

            if (!target.isAlive()){
                logger.info(LogMessages.TARGET_KILLED, target.getName(), attacker.getName());
            }

            int damage = resolveDamage(attacker, target);
            if (damage == -1) return;

            damage = applyDefenseReduction(target, damage);

            if (attacker instanceof Player player) {
                playerService.gainXp(player.getId(), damage);
            }

            if (target instanceof Player player) {
                playerService.takeDamage(player.getId(), damage);
            } else if (target instanceof Npc npc) {
                npcService.takeDamage(npc.getId(), damage);
            }
        }
    }

    private static int resolveDamage(Entity attacker, Entity target) {
        if (attacker instanceof Npc npc) {
            return calculateNpcDamage(npc, target);

        } else if (attacker instanceof Player player) {
            return calculatePlayerDamage(player, target);

        } else {
            throw new UnknownEntityType(attacker.getClass().getSimpleName());
        }
    }

    private static int calculatePlayerDamage(Player attacker, Entity target) {
        int level = attacker.getUser().getLevel();
        int baseDamage = PLAYER_BASE_DAMAGE + level;

        int damage = getEffectiveDamage(attacker, target, baseDamage, PLAYER_MAH_RANGE);
        if (damage == -1) return -1;

        if (attacker.isDoubleDamage()) {
            damage *= 2;
        }

        return damage;
    }

    private static int calculateNpcDamage(Npc attacker, Entity target) {
        int maxRange = switch (attacker.getType()) {
            case ATTACKER -> ATTACKER_MAH_RANGE;
            case GUARD -> GUARD_MAH_RANGE;
        };

        return getEffectiveDamage(attacker, target, NPC_BASE_DAMAGE, maxRange);
    }

    private static int getEffectiveDamage(Entity attacker, Entity target, int baseDamage, int maxRange) {
        int distance = Distance.calculateDistance(attacker.getPosition(), target.getPosition());

        if (distance > maxRange) {
            return -1;
        }

        double distanceFactor = calculateDistanceFactor(distance);
        return (int) (baseDamage * distanceFactor);
    }

    private static int applyDefenseReduction(Entity target, int damage) {
        if (target instanceof Player player) {
            if (player.isDefending() && player.getDefendingEndTime() > System.currentTimeMillis()) {
                return (int) (damage * DEFENSE_DAMAGE_REDUCTION_FACTOR);
            } else {
                player.setDefending(false);
            }
        }
        return damage;
    }

    private static double calculateDistanceFactor(int distance) {
        return Math.max(1.0 - (distance * 0.1), 0.5);
    }
}
