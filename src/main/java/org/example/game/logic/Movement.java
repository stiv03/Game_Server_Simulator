package org.example.game.logic;

import org.example.game.enums.Direction;
import org.example.game.model.Position;

public class Movement {

    public static Position getNewPosition(Position current, Direction direction) {
        int x = current.getX();
        int y = current.getY();

        switch (direction) {
            case UP -> y--;
            case DOWN -> y++;
            case LEFT -> x--;
            case RIGHT -> x++;
        }

        return new Position(x, y);
    }
}
