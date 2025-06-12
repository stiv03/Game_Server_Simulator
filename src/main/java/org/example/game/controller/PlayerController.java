package org.example.game.controller;

import org.example.game.enums.Direction;
import org.example.game.model.Entity;
import org.example.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/{playerId}/move")
    public void move(@PathVariable UUID playerId, @RequestParam Direction direction) throws InterruptedException {
        playerService.move(playerId, direction);
    }

    @PostMapping("/{playerId}/attack")
    public void attack(@PathVariable UUID playerId, @RequestBody Entity target) throws InterruptedException {
        playerService.attack(playerId, target);
    }

    @PostMapping("/{playerId}/defend")
    public void defend(@PathVariable UUID playerId) {
        playerService.defend(playerId);
    }

}
