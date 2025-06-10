package org.example.game.logic;

import org.example.game.model.Position;

import java.util.Random;

public class SpawnLocation {

    private static final int MAX_X = 100;
    private static final int MAX_Y = 100;

    private static final Random random = new Random();

    public static Position getRandomSpawnPosition() {
        int x = random.nextInt(MAX_X);
        int y = random.nextInt(MAX_Y);
        return new Position(x, y);
    }
}
