package org.example.game.logic;

import org.example.game.model.Entity;
import org.example.game.model.Npc;
import org.example.game.model.Player;
import org.example.game.service.NpcService;
import org.example.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Damage {

    private final PlayerService playerService;
    private final NpcService npcService;

    @Autowired
    public Damage(PlayerService playerService, NpcService npcService) {
        this.playerService = playerService;
        this.npcService = npcService;
    }

    public void handleDamage(Entity target, int amount) {
        synchronized (target) {
            if (!target.isAlive()) return;

            if (target instanceof Player player) {
                if (player.isInvincible()) return;
                applyDamageDirectly(player, amount);

            } else if (target instanceof Npc npc) {
                applyDamageDirectly(npc, amount);
            }
        }
    }

    private void applyDamageDirectly(Player player, int amount) {
        int newHealth = player.getHealth() - amount;
        player.setHealth(Math.max(newHealth, 0));

        if (newHealth <= 0) {
            playerService.onDeath(player.getId());
        }
    }

    private void applyDamageDirectly(Npc npc, int amount) {
        int newHealth = npc.getHealth() - amount;
        npc.setHealth(Math.max(newHealth, 0));

        if (newHealth <= 0) {
            npcService.onDeath(npc.getId());
        }
    }
}

