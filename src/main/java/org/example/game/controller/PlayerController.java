package org.example.game.controller;

import org.example.game.enums.Direction;
import org.example.game.gameDto.AttackRequest;
import org.example.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.UUID;

@RestController
@RequestMapping(PlayerApiRoutes.BASE)
public class PlayerController {

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping(PlayerApiRoutes.MOVE)
    public void move(@PathVariable UUID playerId, @RequestParam Direction direction) throws InterruptedException {
        playerService.move(playerId, direction);
    }

    @PostMapping(PlayerApiRoutes.ATTACK)
    public void attack(@PathVariable UUID playerId, @RequestBody AttackRequest attackRequest) throws InterruptedException {
        playerService.attack(playerId, attackRequest.targetId());
    }

    @PostMapping(PlayerApiRoutes.DEFEND)
    public void defend(@PathVariable UUID playerId) {
        playerService.defend(playerId);
    }

}
