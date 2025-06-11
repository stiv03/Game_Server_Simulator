package org.example.game.logic;

import org.example.game.enums.Direction;
import org.example.game.model.Position;

public class Move {

    private static final int MIN_BOUND = 0;
    private static final int MAX_BOUND = 99;


    public static Position getNewPosition(Position current, Direction direction) {
        int x = current.getX();
        int y = current.getY();

        switch (direction) {
            case UP -> {
                if (y > MIN_BOUND) y--;
            }
            case DOWN -> {
                if (y < MAX_BOUND) y++;
            }
            case LEFT -> {
                if (x > MIN_BOUND) x--;
            }
            case RIGHT -> {
                if (x < MAX_BOUND) x++;
            }
        }
        return new Position(x, y);
    }
}
