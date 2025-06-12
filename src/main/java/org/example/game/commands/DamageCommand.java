package org.example.game.commands;

import org.example.config.messages.LogMessages;
import org.example.game.logic.Damage;
import org.example.game.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DamageCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(DamageCommand.class);

    private final int damageAmount;
    private final Damage damageLogic;

    public DamageCommand(int damageAmount, Damage damageLogic) {
        this.damageAmount = damageAmount;
        this.damageLogic = damageLogic;
    }

    @Override
    public void execute(Entity entity) {
        damageLogic.handleDamage(entity, damageAmount);
        logger.info(LogMessages.DAMAGE_TAKEN, entity.getName(), damageAmount);
    }
}
