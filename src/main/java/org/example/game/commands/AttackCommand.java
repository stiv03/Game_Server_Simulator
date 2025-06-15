package org.example.game.commands;

import org.example.config.messages.LogMessages;
import org.example.game.logic.Attack;
import org.example.game.logic.gameSession.GameSessionContext;
import org.example.game.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class AttackCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(AttackCommand.class);

    private final UUID targetId;
    private final Attack attackService;
    private final GameSessionContext context;

    public AttackCommand(UUID targetId, Attack attackService, GameSessionContext context) {
        this.targetId = targetId;
        this.attackService = attackService;
        this.context = context;
    }

    @Override
    public void execute(Entity attacker) throws Exception {
        Entity target = context.getEntitiesMap().get(targetId);

        if (target == null) {
            logger.warn("Cannot attack: target is null (maybe left session).");
            return;
        }

        attackService.hitTarget(attacker, target);
        logger.info("{} attacked {}", attacker.getName(), target.getName());
    }
}

