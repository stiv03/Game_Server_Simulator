package org.example.game.logic;

import org.example.game.model.Position;


import org.example.game.model.GameMap;

public class SpawnLocation {

    private final GameMap gameMap;

    public SpawnLocation(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    public Position getValidSpawnPosition() {
        return gameMap.getRandomEmptyPosition();
    }
}