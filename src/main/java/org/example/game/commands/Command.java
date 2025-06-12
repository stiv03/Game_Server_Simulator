package org.example.game.commands;

import org.example.game.model.Entity;


public interface Command {
    void execute(Entity entity) throws Exception;
}
