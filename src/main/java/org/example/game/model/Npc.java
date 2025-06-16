package org.example.game.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.example.game.enums.NpcType;
import org.example.game.commands.Command;
import org.example.game.logic.npcAutomations.NpcAiEngine;
import org.example.persistence.entity.GameSession;


import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
@Setter
public class Npc implements Entity, Runnable {

    private static final int MAX_NPC_HEALTH = 50;

    private final GameSession session;

    private final UUID id = UUID.randomUUID();
    private final String name;
    private final NpcType type;
    private Position position;
    private int health = MAX_NPC_HEALTH;
    private boolean defending = false;

    private volatile boolean running = true;

    @JsonIgnore
    private final NpcAiEngine npcAiEngine;

    @JsonIgnore
    private final BlockingQueue<Command> commandQueue = new LinkedBlockingQueue<>();


    public Npc(NpcType type, Position position, GameSession session, NpcAiEngine npcAiEngine) {
        this.type = type;
        this.position = position;
        this.name = type.toString().toLowerCase() + id;
        this.session = session;
        this.npcAiEngine = npcAiEngine;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Command command = commandQueue.poll();
                if (command != null) {
                    command.execute(this);
                } else {
                    npcAiEngine.automaticAct(this);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @Override
    public void disconnect() {
        this.running = false;
        commandQueue.offer(command -> {
        });
    }
}

