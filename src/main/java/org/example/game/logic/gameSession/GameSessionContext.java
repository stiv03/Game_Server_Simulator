package org.example.game.logic.gameSession;

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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Scope("prototype")
@Component
public class GameSessionContext implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(GameSessionContext.class);

    private static final int EASY_NPC_COUNT = 1;
    private static final int NORMAL_NPC_COUNT = 2;
    private static final int HARD_NPC_COUNT = 5;

    private static final int EASY_ITEM_COUNT = 20;
    private static final int NORMAL_ITEM_COUNT = 10;
    private static final int HARD_ITEM_COUNT = 5;

    private GameSession session;

    private final NpcService npcService;

    private final ItemService itemService;

    private final Map<UUID, Thread> playerThreads = new HashMap<>();

    private final GameSessionRepository sessionRepository;

    private final List<Entity> entities = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private volatile boolean running = true;


    public GameSessionContext(GameSessionRepository sessionRepository, NpcService npcService, ItemServiceImpl itemService) {
        this.sessionRepository = sessionRepository;
        this.npcService = npcService;
        this.itemService = itemService;
    }

    public void load(UUID sessionId) {
        this.session = sessionRepository.findById(sessionId)
                .orElseThrow(SessionNotFoundException::new);
    }


    public void startSession() {

        if (session == null) throw new SessionNotFoundException();
        logger.info(LogMessages.STARTING_SESSION, session.getId());
        spawnNpcsByDifficulty(session.getDifficulty());
        spawnItemByDifficulty(session.getDifficulty());

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
            entities.add(npc);
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


    public void joinPlayer(Player player) {
        entities.add(player);


        logger.info(LogMessages.PLAYER_JOIN, player.getName(), session.getId(), player.getPosition().toString());
    }

    public List<Entity> getRanking() {
        List<Entity> sorted = entities.stream()
                .sorted(Comparator.comparingInt(Entity::getHealth).reversed())
                .toList();

        logger.info(LogMessages.RANKING, session.getId());
        for (int i = 0; i < sorted.size(); i++) {
            Entity e = sorted.get(i);
            logger.info(LogMessages.ENTITY_RANKING, i + 1, e.getName(), e.getHealth());
        }

        return sorted;
    }

    public void removePlayer(UUID userId) {
        Player toRemove = null;
        for (Entity entity : entities) {
            if (entity instanceof Player player && player.getUser().getId().equals(userId)) {
                toRemove = player;
                break;
            }
        }

        if (toRemove != null) {
            toRemove.disconnect();
            entities.remove(toRemove);
            logger.info(LogMessages.DISCONNECT_SESSION, toRemove.getName(), session.getId());
        } else {
            logger.warn(LogMessages.PLAYER_NOT_IN_SESSION, userId, session.getId());
        }
    }

    public void pickUpItem(Player player) {
        for (Item item : items) {
            if (!item.isConsumed() && item.getPosition().equals(player.getPosition())) {
                itemService.applyEffect(item, player);
                item.setConsumed(true);
            }
        }
    }


    @Override
    public void run() {
        logger.info(LogMessages.START_GAME_SESSION, session.getId());
        while (running) {
            try {
                Thread.sleep(1000);
                //синхенонизирай тук потоците от плейъри и нпс
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.info(LogMessages.INTERRUPTED_GAME_SESSION, session.getId());
            }
        }
        logger.info(LogMessages.STOPPED_GAME_SESSION, session.getId());
    }

    public void stop() {
        this.running = false;

        for (Entity e : entities) {
            e.disconnect();

        }
        logger.info(LogMessages.STOP_ALL_ENTITIES_IN_SESSION, session.getId());
    }
}
