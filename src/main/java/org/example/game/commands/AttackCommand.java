package org.example.game.commands;

import org.example.config.messages.LogMessages;
import org.example.game.logic.Attack;
import org.example.game.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttackCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(AttackCommand.class);

    private final Entity target;
    private final Attack attackService;

    public AttackCommand(Entity target, Attack attackService) {
        this.target = target;
        this.attackService = attackService;
    }

    @Override
    public void execute(Entity attacker) throws Exception {
        attackService.hitTarget(attacker, target);
        logger.info(LogMessages.ATTACK_LOG, attacker.getName(), target.getName());
    }
}

