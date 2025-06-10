package org.example.game.logic;

import org.example.config.messages.LogMessages;
import org.example.exceptions.UnknownAttackerType;
import org.example.game.core.Entity;
import org.example.game.model.Npc;
import org.example.game.model.Player;
import org.example.game.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Attack {

    private final static int PLAYER_BASE_DAMAGE = 5;
    private final static int NPC_BASE_DAMAGE = 10;

    private final static int PLAYER_MAH_RANGE = 5;
    private final static int ATTACKER_MAH_RANGE = 10;
    private final static int GUARD_MAH_RANGE = 2;

    private static final double DEFENSE_DAMAGE_REDUCTION_FACTOR = 0.5;


    private static final Logger logger = LoggerFactory.getLogger(Attack.class);

    public static void performAttack(Entity attacker, Entity target) {
        if (!target.isAlive()) return;

        int damage = resolveDamage(attacker, target);
        if (damage == -1) return;

        damage = applyDefenseReduction(target, damage);

        if (attacker instanceof Player player) {
            player.gainXp(damage);
        }

        target.takeDamage(damage);

        logger.info(LogMessages.ATTACK_LOG, attacker.getName(), target.getName(), damage);
    }


    private static int resolveDamage(Entity attacker, Entity target) {
        if (attacker instanceof Npc npc) {
            return calculateNpcDamage(npc, target);

        } else if (attacker instanceof Player player) {
            return calculatePlayerDamage(player, target);

        } else {
            throw new UnknownAttackerType(attacker.getClass().getSimpleName());
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
        int distance = calculateDistance(attacker.getPosition(), target.getPosition());

        if (distance > maxRange) {
            return -1;
        }

        double distanceFactor = calculateDistanceFactor(distance);
        return (int) (baseDamage * distanceFactor);
    }


    private static int applyDefenseReduction(Entity target, int damage) {
        if (target.isDefending()) {
            return (int) (damage * DEFENSE_DAMAGE_REDUCTION_FACTOR);
        }
        return damage;
    }


    private static int calculateDistance(Position attacker, Position target) {
        int dx = attacker.getX() - target.getX();
        int dy = attacker.getY() - target.getY();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }


    private static double calculateDistanceFactor(int distance) {
        return Math.max(1.0 - (distance * 0.1), 0.5);
    }
}
