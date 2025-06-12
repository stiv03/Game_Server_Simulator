package org.example.game.logic.helpers;

import org.example.game.model.Position;

public class Distance {

    public static int calculateDistance(Position a, Position b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }
}
