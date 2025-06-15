package org.example.game.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.example.config.messages.LogMessages;
import org.example.game.commands.Command;
import org.example.persistence.entity.GameSession;
import org.example.persistence.entity.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
@Setter
public class Player implements Entity, Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Player.class);

    private static final int MAX_HEALTH = 100;

    private Users user;
    private Position position;

    private final GameSession session;

    private int health = MAX_HEALTH;

    private int xp;
    private boolean idDead;
    private boolean defending;
    private boolean doubleDamage;
    private boolean invincible;
    private boolean speedBoost;

    private long doubleDamageEndTime = 0;
    private long invincibilityEndTime = 0;
    private long speedBoostEndTime = 0;

    private long defendingEndTime;


    private volatile boolean running = true;
    @JsonIgnore
    private final BlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();

    public Player(Users user, Position position, GameSession session) {
        this.user = user;
        this.position = position;
        this.session = session;
    }

    public UUID getId() {
        return user.getId();
    }

    public String getName() {
        return user.getUsername();
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Command command = commandQueue.take();
                command.execute(this);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        logger.info(LogMessages.KILL_PLAYER_THREAD);
    }

    @Override
    public void disconnect() {
        this.running = false;
        commandQueue.offer(command -> {
        });
    }
}