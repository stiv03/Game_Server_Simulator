package org.example.game.service.serviceIml;

import org.example.config.messages.LogMessages;
import org.example.game.enums.Effect;
import org.example.game.model.GameMap;
import org.example.game.model.Item;
import org.example.game.model.Player;
import org.example.game.model.Position;
import org.example.game.service.ItemService;
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

    public Item spawnItemsForSession(GameSession session) {

        GameMap map = session.getGameMap();
        Position droppedAtPosition = map.getRandomEmptyPosition();
        return new Item(droppedAtPosition, randomEffect());
    }

    @Override
    public void applyEffect(Item item, Player player) {
        if (item.isConsumed() || !player.getPosition().equals(item.getPosition())) return;

        logger.info(LogMessages.LOG_ITEM_PICKED, player.getName(), item.getEffect(),
                item.getPosition().getX(), item.getPosition().getY());

        switch (item.getEffect()) {
            case HEALTH_RECOVERY -> {
                player.setHealth(Math.min(MAX_HEALTH, player.getHealth() + HEALTH_RECOVERY_POINTS));
                logger.info(LogMessages.LOG_HEALTH_RECOVERY, player.getName());
            }
            case DOUBLE_DAMAGE -> {
                player.setDoubleDamage(true);
                player.setDoubleDamageEndTime(System.currentTimeMillis() + EFFECT_DURATION_MILLIS);
                logger.info(LogMessages.LOG_DOUBLE_DAMAGE, player.getName());
            }
            case INVINCIBILITY -> {
                player.setInvincible(true);
                player.setInvincibilityEndTime(System.currentTimeMillis() + EFFECT_DURATION_MILLIS);
                logger.info(LogMessages.LOG_INVINCIBILITY, player.getName());
            }
            case SPEED_BOOST -> {
                player.setSpeedBoost(true);
                player.setSpeedBoostEndTime(System.currentTimeMillis() + EFFECT_DURATION_MILLIS);
                logger.info(LogMessages.LOG_SPEED_BOOST, player.getName());
            }
        }

        item.setConsumed(true);
        logger.debug(LogMessages.LOG_ITEM_CONSUMED,
                item.getPosition().getX(), item.getPosition().getY());
    }

    private Effect randomEffect() {
        Effect[] effects = Effect.values();
        return effects[new Random().nextInt(effects.length)];
    }
}
