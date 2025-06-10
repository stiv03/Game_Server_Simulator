package org.example.game.logic;

import org.example.game.core.Entity;
import org.example.game.enums.NpcType;
import org.example.game.model.Npc;
import org.example.game.model.Player;
import org.example.game.model.Position;

public class Attack {

    private final static int PLAYER_BASE_DAMAGE = 5;
    private final static int NPC_BASE_DAMAGE = 10;

    private final static int PLAYER_MAH_RANGE = 5;
    private final static int ATTACKER_MAH_RANGE = 10;
    private final static int GUARD_MAH_RANGE = 2;

    public static void performAttack(Entity attacker, Entity target) {
        if (!target.isAlive()) return;

        int damage = resolveDamage(attacker, target);
        if (damage == -1) return;

        if (target.isDefending()) {
            damage /= 2;
        }

        target.takeDamage(damage);

        System.out.printf("%s attacked %s for %d damage%n", attacker.getName(), target.getName(), damage);
    }


    private static int resolveDamage(Entity attacker, Entity target) {

        if (attacker instanceof Npc npc) {
            return calculateNpcDamage(npc, target);
        } else if (attacker instanceof Player player) {
            return calculatePlayerDamage(player, attacker.getPosition(), target);
        } else {
            throw new IllegalArgumentException("Unknown attacker type: " + attacker.getClass().getSimpleName());
        }
    }


    private static int calculatePlayerDamage(Player player, Position attackerPos, Entity target) {
        int level = player.getUser().getLevel();
        int baseDamage = PLAYER_BASE_DAMAGE + level;

        int distance = calculateDistance(attackerPos, target.getPosition());
        if (distance > PLAYER_MAH_RANGE) {
            System.out.println("Target is too far for player attack.");
            return -1;
        }

        double distanceFactor = calculateDistanceFactor(distance, 0.1, 0.5);

        return (int) (baseDamage * distanceFactor);
    }

    private static int calculateNpcDamage(Npc npc, Entity target) {

        NpcType type = npc.getType();
        int distance = calculateDistance(npc.getPosition(), target.getPosition());


        if (type == NpcType.ATTACKER) {
            if (distance > ATTACKER_MAH_RANGE) {
                System.out.println("Target is too far for ATTACKER NPC.");
                return -1;
            }
        } else if (type == NpcType.GUARD) {
            if (distance > GUARD_MAH_RANGE) {
                System.out.println("GUARD NPC ignores target that is not close.");
                return -1;
            }
        }

        double distanceFactor = calculateDistanceFactor(distance, 0.05, 0.7);

        return (int) (NPC_BASE_DAMAGE * distanceFactor);
    }

    private static int calculateDistance(Position a, Position b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }

    private static double calculateDistanceFactor(int distance, double multiplier, double minFactor) {
        return Math.max(1.0 - (distance * multiplier), minFactor);
    }
}
