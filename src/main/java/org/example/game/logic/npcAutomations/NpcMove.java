package org.example.game.logic.npcAutomations;

import org.example.game.enums.Direction;
import org.example.game.logic.Distance;
import org.example.game.model.Npc;
import org.example.game.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class NpcMove {
    private static final Random random = new Random();
    private static final Logger logger = LoggerFactory.getLogger(NpcMove.class);


    public static void moveNpc(Npc npc, List<Player> players) {
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

    private static void chasePlayer(Npc npc, Player target)  {
        int dx = target.getPosition().getX() - npc.getPosition().getX();
        int dy = target.getPosition().getY() - npc.getPosition().getY();

        Direction dir;
        if (Math.abs(dx) > Math.abs(dy)) {
            dir = dx > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            dir = dy > 0 ? Direction.DOWN : Direction.UP;
        }

        npc.move(dir);
    }

    private static void moveRandomly(Npc npc)  {
        Direction[] directions = Direction.values();
        Direction randomDir = directions[random.nextInt(directions.length)];
        npc.move(randomDir);
    }

    private static Player findClosestPlayer(Npc npc, List<Player> players) {
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
