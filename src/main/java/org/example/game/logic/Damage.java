package org.example.game.logic;

import org.example.game.model.Entity;
import org.example.game.model.Player;

public class Damage {

    public static void handleDamage(Entity target, int amount) {
        if (!target.isAlive()) return;

        if (target instanceof Player player && player.isInvincible()) {
            return;
        }

        int newHealth = target.getHealth() - amount;
        target.setHealth(Math.max(newHealth, 0));

        if (newHealth <= 0) {
            target.onDeath();
        }
    }
}
