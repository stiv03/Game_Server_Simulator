package org.example.game.logic.npcAutomations;

import org.example.game.model.Npc;
import org.example.game.service.PlayerService;

public class NpcAiEngine {

    private final NpcMove npcMove;
    private final NpcAttack npcAttack;
    private final PlayerService playerService;

    public NpcAiEngine(NpcMove npcMove, NpcAttack npcAttack, PlayerService playerService) {
        this.npcMove = npcMove;
        this.npcAttack = npcAttack;
        this.playerService = playerService;
    }

    public void automaticAct(Npc npc) throws InterruptedException {
        Thread.sleep(10000);
        npcMove.moveNpc(npc, playerService.getAllPlayers());
        npcAttack.tryAttack(npc, playerService.getAllPlayers());
    }
}
