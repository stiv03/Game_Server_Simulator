package org.example.game.logic.gameSession;

import lombok.Getter;
import org.example.config.messages.LogMessages;
import org.example.exceptions.SessionNotFoundException;
import org.example.game.model.Entity;
import org.example.game.model.Item;
import org.example.game.model.Npc;
import org.example.game.model.Player;
import org.example.game.service.ItemService;
import org.example.game.service.NpcService;
import org.example.game.service.serviceIml.ItemServiceImpl;
import org.example.persistence.entity.GameSession;
import org.example.persistence.entity.enums.GameDifficulty;
import org.example.persistence.repository.GameSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Scope("prototype")
@Component
public class GameSessionContext implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GameSessionContext.class);

    private static final int EASY_NPC_COUNT = 100;
    private static final int NORMAL_NPC_COUNT = 150;
    private static final int HARD_NPC_COUNT = 300;

    private static final int EASY_ITEM_COUNT = 200;
    private static final int NORMAL_ITEM_COUNT = 100;
    private static final int HARD_ITEM_COUNT = 50;

    private GameSession session;

    private final NpcService npcService;

    private final ItemService itemService;

    private final GameSessionRepository sessionRepository;

    private final Map<UUID, Entity> entities = new ConcurrentHashMap<>();
    @Getter
    private final List<Item> items = Collections.synchronizedList(new ArrayList<>());
    private volatile boolean running = true;


    public GameSessionContext(GameSessionRepository sessionRepository, NpcService npcService, ItemServiceImpl itemService) {
        this.sessionRepository = sessionRepository;
        this.npcService = npcService;
        this.itemService = itemService;
    }

    @Override
    public void run() {
        logger.info(LogMessages.START_GAME_SESSION, session.getId());
        while (running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.info(LogMessages.INTERRUPTED_GAME_SESSION, session.getId());
            }
        }
        logger.info(LogMessages.STOPPED_GAME_SESSION, session.getId());
    }

    public void loadSession(UUID sessionId) {
        this.session = sessionRepository.findById(sessionId)
                .orElseThrow(SessionNotFoundException::new);
    }


    public void startSession() {
        if (session == null) throw new SessionNotFoundException();
        logger.info(LogMessages.STARTING_SESSION, session.getId());
        spawnNpcsByDifficulty(session.getDifficulty());
        spawnItemByDifficulty(session.getDifficulty());
    }


    public void joinPlayer(Player player) {
        entities.put(player.getId(), player);
        logger.info(LogMessages.PLAYER_JOIN, player.getName(), session.getId(), player.getPosition().toString());
    }

    public List<Entity> getRanking() {
        List<Entity> sortedRanking = entities.values().stream()
                .sorted(Comparator.comparingInt(Entity::getHealth).reversed())
                .toList();

        logger.info(LogMessages.RANKING, session.getId());
        for (int i = 0; i < sortedRanking.size(); i++) {
            Entity entity = sortedRanking.get(i);
            logger.info(LogMessages.ENTITY_RANKING, i + 1, entity.getName(), entity.getHealth());
        }

        return sortedRanking;
    }


    public void removePlayer(UUID userId) {
        Player toRemove = null;
        for (Entity entity : entities.values()) {
            if (entity instanceof Player player && player.getUser().getId().equals(userId)) {
                toRemove = player;
                break;
            }
        }

        if (toRemove != null) {
            toRemove.disconnect();
            entities.remove(toRemove.getId());
            logger.info(LogMessages.DISCONNECT_SESSION, toRemove.getName(), session.getId());
        } else {
            logger.warn(LogMessages.PLAYER_NOT_IN_SESSION, userId, session.getId());
        }
    }

    public void pickUpItem(Player player) {
        synchronized (items) {
            for (Item item : items) {
                if (!item.isConsumed() && item.getPosition().equals(player.getPosition())) {
                    itemService.applyEffect(item, player);
                }
            }
        }
    }

    public Map<UUID, Entity> getEntitiesMap() {
        return entities;
    }


    public void stop() {
        this.running = false;
        for (Entity e : entities.values()) {
            e.disconnect();
        }
        logger.info(LogMessages.STOP_ALL_ENTITIES_IN_SESSION, session.getId());
    }

    private void spawnNpcsByDifficulty(GameDifficulty difficulty) {
        int count = switch (difficulty) {
            case EASY -> EASY_NPC_COUNT;
            case HARD -> HARD_NPC_COUNT;
            default -> NORMAL_NPC_COUNT;
        };

        for (int i = 0; i < count; i++) {
            Npc npc = npcService.spawnNpc(session);
            logger.info(LogMessages.SPAWNED_NPC,
                    npc.getPosition().toString(), session.getId());
            entities.put(npc.getId(), npc);
        }
    }

    private void spawnItemByDifficulty(GameDifficulty difficulty) {
        int count = switch (difficulty) {
            case EASY -> EASY_ITEM_COUNT;
            case HARD -> HARD_ITEM_COUNT;
            default -> NORMAL_ITEM_COUNT;
        };
        for (int i = 0; i < count; i++) {
            Item item = itemService.spawnItemsForSession(session);
            logger.info(LogMessages.DROPPED_ITEM, item.getEffect(),
                    item.getPosition().toString(), session.getId());
            items.add(item);
        }
    }
}
