package org.example.game.logic.npcAutomations;

import org.example.game.logic.Distance;
import org.example.game.model.Npc;
import org.example.game.model.Player;

import java.util.List;

public class NpcAttack {

    private final static int ATTACKER_RANGE = 10;
    private final static int GUARD_RANGE = 2;

    public static void tryAttack(Npc npc, List<Player> players) {
        for (Player player : players) {
            if (!player.isAlive()) continue;

            int distance = Distance.calculateDistance(npc.getPosition(), player.getPosition());

            int attackRange = switch (npc.getType()) {
                case ATTACKER -> ATTACKER_RANGE;
                case GUARD -> GUARD_RANGE;
            };

            if (distance <= attackRange) {
                npc.attack(player);
                return;
            }
        }
    }
}
