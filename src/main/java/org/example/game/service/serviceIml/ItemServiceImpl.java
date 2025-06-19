package org.example.game.service.serviceIml;

import org.example.config.lock.LockManager;
import org.example.config.messages.LogMessages;
import org.example.game.enums.Effect;
import org.example.game.model.GameMap;
import org.example.game.model.Item;
import org.example.game.model.Player;
import org.example.game.model.Position;
import org.example.game.service.ItemService;
import org.example.game.service.PlayerService;
import org.example.persistence.entity.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    private static final int HEALTH_RECOVERY_POINTS = 30;
    private static final int MAX_HEALTH = 100;
    private static final int EFFECT_DURATION_MILLIS = 30_000;

    private final PlayerService playerService;

    public ItemServiceImpl(PlayerService playerService) {
        this.playerService = playerService;
    }


    public Item spawnItemsForSession(GameSession session) {
        GameMap map = session.getGameMap();
        Position droppedAtPosition = map.getRandomEmptyPosition();
        return new Item(droppedAtPosition, randomEffect());
    }

    @Override
    public void applyEffect(Item item, Player player) {
        if (!player.getPosition().equals(item.getPosition())) return;
        if (!item.consume()) return;

        logger.info(LogMessages.LOG_ITEM_PICKED, player.getName(), item.getEffect(),
                item.getPosition().getX(), item.getPosition().getY());

        switch (item.getEffect()) {
            case HEALTH_RECOVERY -> {
                playerService.applyHealthRecovery(player, HEALTH_RECOVERY_POINTS, MAX_HEALTH);
                logger.info(LogMessages.LOG_HEALTH_RECOVERY, player.getName());
            }
            case DOUBLE_DAMAGE -> {
                playerService.applyDoubleDamage(player, EFFECT_DURATION_MILLIS);
                logger.info(LogMessages.LOG_DOUBLE_DAMAGE, player.getName());
            }
            case INVINCIBILITY -> {
                playerService.applyInvincibility(player, EFFECT_DURATION_MILLIS);
                logger.info(LogMessages.LOG_INVINCIBILITY, player.getName());
            }
            case SPEED_BOOST -> {
                playerService.applySpeedBoost(player, EFFECT_DURATION_MILLIS);
                logger.info(LogMessages.LOG_SPEED_BOOST, player.getName());
            }
        }

        logger.debug(LogMessages.LOG_ITEM_CONSUMED,
                item.getPosition().getX(), item.getPosition().getY());
        LockManager.removeLock(item.getId());
    }


    private Effect randomEffect() {
        Effect[] effects = Effect.values();
        return effects[new Random().nextInt(effects.length)];
    }
}
