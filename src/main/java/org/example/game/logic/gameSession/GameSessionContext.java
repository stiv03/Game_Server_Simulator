package org.example.game.logic.gameSession;

import org.example.config.messages.LogMessages;
import org.example.game.enums.NpcType;
import org.example.game.model.Entity;
import org.example.game.model.GameMap;
import org.example.game.model.Npc;
import org.example.game.model.Position;
import org.example.persistence.entity.GameSession;
import org.example.persistence.entity.enums.GameDifficulty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameSessionContext {

    private static final Logger logger = LoggerFactory.getLogger(GameSessionContext.class);

    private static final int EASY_NPC_COUNT = 10;
    private static final int NORMAL_NPC_COUNT = 20;
    private static final int HARD_NPC_COUNT = 50;

    private final GameSession session;
    private final GameMap map;
    private final List<Entity> entities = new ArrayList<>();


    public GameSessionContext(GameSession session) {
        this.session = session;
        this.map = new GameMap();
        spawnNPCs();
    }


    // премести
    private void spawnNPCs() {

        GameDifficulty difficulty = session.getDifficulty();
        int count;
        switch (difficulty) {
            case EASY -> count = EASY_NPC_COUNT;
            case HARD -> count = HARD_NPC_COUNT;
            default -> count = NORMAL_NPC_COUNT;
        }


        for (int i = 0; i < count; i++) {
            Position spawnPos = map.getRandomEmptyPosition();
            NpcType type = getRandomNpcType();
            Npc npc = new Npc(type, spawnPos, session);
            entities.add(npc);
            logger.info(LogMessages.SPAWNED_NPC, type, spawnPos);
        }
    }

    private NpcType getRandomNpcType() {
        NpcType[] types = NpcType.values();
        return types[new Random().nextInt(types.length)];
    }




    //гейм сесията

    //картата

    //спаунване на нпс-та, айтъми, спррямо difficulty-to гтв

    //показване на ранка с животите

    // гейм-лооп-а вади евентите

    //като се джоине
}
