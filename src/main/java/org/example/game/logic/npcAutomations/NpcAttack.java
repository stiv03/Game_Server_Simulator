package org.example.game.logic.npcAutomations;

import org.example.game.logic.helpers.Distance;
import org.example.game.model.Npc;
import org.example.game.model.Player;
import org.example.game.service.NpcService;

import java.util.Collection;

public class NpcAttack {

    private final static int ATTACKER_RANGE = 10;
    private final static int GUARD_RANGE = 2;

    private final NpcService npcService;

    public NpcAttack(NpcService npcService) {
        this.npcService = npcService;
    }

    public void tryAttack(Npc npc, Collection<Player> players) throws InterruptedException {
        for (Player player : players) {
            if (!player.isAlive()) continue;

            int distance = Distance.calculateDistance(npc.getPosition(), player.getPosition());

            int attackRange = switch (npc.getType()) {
                case ATTACKER -> ATTACKER_RANGE;
                case GUARD -> GUARD_RANGE;
            };

            if (distance <= attackRange) {
                npcService.attack(npc.getId(), player.getId());
                return;
            }
        }
    }
}
