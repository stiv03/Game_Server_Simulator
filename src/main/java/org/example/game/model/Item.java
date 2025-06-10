package org.example.game.model;

import lombok.Getter;
import lombok.Setter;
import org.example.config.messages.LogMessages;
import org.example.game.enums.Effect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class Item {

    private static final Logger logger = LoggerFactory.getLogger(Item.class);

    private static final int HEALTH_RECOVERY_POINTS = 30;
    private static final int MAH_HEALTH = 100;
    private static final int EFFECT_DURATION_SECONDS = 30_000;


    private Position position;
    private Effect effect;
    private boolean consumed = false;


    public void pickUpItem(Player player) {
        synchronized (this) {
            if (!consumed && player.getPosition().equals(position)) {
                logger.info(LogMessages.LOG_ITEM_PICKED, player.getName(), effect, position.getX(), position.getY());
                applyEffect(player);
            }
        }
    }

    private void applyEffect(Player player) {

        switch (effect) {
            case HEALTH_RECOVERY -> {
                player.setHealth(Math.min(MAH_HEALTH, player.getHealth() + HEALTH_RECOVERY_POINTS));
                logger.info(LogMessages.LOG_HEALTH_RECOVERY, player.getName());
            }
            case DOUBLE_DAMAGE -> {
                player.setDoubleDamage(true);
                player.setDoubleDamageEndTime(System.currentTimeMillis() + EFFECT_DURATION_SECONDS);
                logger.info(LogMessages.LOG_DOUBLE_DAMAGE, player.getName());
            }
            case INVINCIBILITY -> {
                player.setInvincible(true);
                player.setInvincibilityEndTime(System.currentTimeMillis() + EFFECT_DURATION_SECONDS);
                logger.info(LogMessages.LOG_INVINCIBILITY, player.getName());
            }
            case SPEED_BOOST -> {
                player.setSpeedBoost(true);
                player.setSpeedBoostEndTime(System.currentTimeMillis() + EFFECT_DURATION_SECONDS);
                logger.info(LogMessages.LOG_SPEED_BOOST, player.getName());
            }
        }
        consumed = true;
        logger.debug(LogMessages.LOG_ITEM_CONSUMED, position.getX(), position.getY());
    }
}
