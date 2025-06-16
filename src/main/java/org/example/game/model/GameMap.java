package org.example.game.model;

import lombok.Getter;
import org.example.game.enums.Tile;

import java.util.Random;
import java.util.Stack;

@Getter
public class GameMap {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;

    private final Tile[][] tiles = new Tile[HEIGHT][WIDTH];
    private final Random random = new Random();

    public GameMap() {
        generateMaze();
    }

    private void generateMaze() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                tiles[y][x] = Tile.WALL;
            }
        }

        int startX = 1;
        int startY = 1;
        tiles[startY][startX] = Tile.EMPTY;

        Stack<Position> stack = new Stack<>();
        stack.push(new Position(startX, startY));

        while (!stack.isEmpty()) {
            Position current = stack.peek();
            Position next = getRandomUnvisitedNeighbor(current);

            if (next != null) {
                int wallX = (current.getX() + next.getX()) / 2;
                int wallY = (current.getY() + next.getY()) / 2;
                tiles[wallY][wallX] = Tile.EMPTY;
                tiles[next.getY()][next.getX()] = Tile.EMPTY;

                stack.push(next);
            } else {
                stack.pop();
            }
        }
    }

    private Position getRandomUnvisitedNeighbor(Position pos) {
        int x = pos.getX();
        int y = pos.getY();

        Position[] directions = {
                new Position(x + 2, y),
                new Position(x - 2, y),
                new Position(x, y + 2),
                new Position(x, y - 2)
        };

        for (int i = 0; i < directions.length; i++) {
            int j = random.nextInt(directions.length);
            Position tmp = directions[i];
            directions[i] = directions[j];
            directions[j] = tmp;
        }

        for (Position next : directions) {
            if (isInBounds(next) && tiles[next.getY()][next.getX()] == Tile.WALL) {
                return next;
            }
        }

        return null;
    }

    private boolean isInBounds(Position pos) {
        return pos.getX() > 0 && pos.getX() < WIDTH - 1 && pos.getY() > 0 && pos.getY() < HEIGHT - 1;
    }


    public Position getRandomEmptyPosition() {
        while (true) {
            int x = random.nextInt(WIDTH);
            int y = random.nextInt(HEIGHT);
            if (tiles[y][x] == Tile.EMPTY) {
                return new Position(x, y);
            }
        }
    }

}
