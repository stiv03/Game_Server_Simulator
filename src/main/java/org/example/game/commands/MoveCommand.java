package org.example.game.commands;

import org.example.config.messages.LogMessages;
import org.example.game.enums.Direction;
import org.example.game.logic.Move;
import org.example.game.logic.gameSession.GameSessionContext;
import org.example.game.model.Entity;
import org.example.game.model.Player;
import org.example.game.model.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger(MoveCommand.class);

    private final Direction direction;
    private final GameSessionContext context;

    public MoveCommand(Direction direction, GameSessionContext context) {
        this.direction = direction;
        this.context = context;
    }

    @Override
    public void execute(Entity entity) {
        Position newPosition = Move.getNewPosition(entity.getPosition(), direction);
        entity.setPosition(newPosition);
        logger.info(LogMessages.MOVE_TO, entity.getName(), direction, newPosition);

        if (entity instanceof Player player) {
            logger.debug("Player {} stepped on {}, checking for item pickup", player.getName(), player.getPosition());
            context.pickUpItem(player);
        }
    }
}
