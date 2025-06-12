package org.example.game.logic.npcAutomations;

import org.example.game.enums.Direction;
import org.example.game.logic.helpers.Distance;
import org.example.game.model.Npc;
import org.example.game.model.Player;
import org.example.game.service.NpcService;

import java.util.Collection;
import java.util.Random;

public class NpcMove {

    private final NpcService npcService;

    private static final Random random = new Random();

    public NpcMove(NpcService npcService) {
        this.npcService = npcService;
    }
    public  void moveNpc(Npc npc, Collection<Player> players) throws InterruptedException {
        switch (npc.getType()) {
            case ATTACKER -> {
                Player closest = findClosestPlayer(npc, players);
                if (closest != null) {
                    chasePlayer(npc, closest);
                }
            }
            case GUARD -> moveRandomly(npc);
        }
    }

    private void chasePlayer(Npc npc, Player target) throws InterruptedException {
        int dx = target.getPosition().getX() - npc.getPosition().getX();
        int dy = target.getPosition().getY() - npc.getPosition().getY();

        Direction dir;
        if (Math.abs(dx) > Math.abs(dy)) {
            dir = dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            dir = dy > 0 ? Direction.DOWN : Direction.UP;
        }

        npcService.move(npc.getId(), dir);
    }

    private void moveRandomly(Npc npc) throws InterruptedException {
        Direction[] directions = Direction.values();
        Direction randomDir = directions[random.nextInt(directions.length)];
        npcService.move(npc.getId(), randomDir);
    }

    private static Player findClosestPlayer(Npc npc, Collection<Player> players) {
        Player closest = null;
        int minDistance = Integer.MAX_VALUE;

        for (Player player : players) {
            int distance = Distance.calculateDistance(npc.getPosition(), player.getPosition());
            if (distance < minDistance) {
                minDistance = distance;
                closest = player;
            }
        }

        return closest;
    }
}
