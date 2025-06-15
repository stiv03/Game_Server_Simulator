package org.example.game.service.serviceIml;

import org.example.config.messages.LogMessages;
import org.example.game.enums.Direction;
import org.example.game.enums.NpcType;
import org.example.game.commands.AttackCommand;
import org.example.game.commands.DamageCommand;
import org.example.game.commands.MoveCommand;
import org.example.game.logic.Attack;
import org.example.game.logic.Damage;
import org.example.game.logic.gameSession.GameSessionContext;
import org.example.game.logic.gameSession.GameSessionManager;
import org.example.game.logic.npcAutomations.NpcAiEngine;
import org.example.game.logic.npcAutomations.NpcAttack;
import org.example.game.logic.npcAutomations.NpcMove;
import org.example.game.model.Entity;
import org.example.game.model.GameMap;
import org.example.game.model.Npc;
import org.example.game.model.Position;
import org.example.game.service.NpcService;
import org.example.game.service.PlayerService;
import org.example.persistence.entity.GameSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NpcServiceImpl implements NpcService {

    private static final Logger logger = LoggerFactory.getLogger(NpcServiceImpl.class);
    private static final int NORMAL_STEP = 1;

    @Autowired
    private GameSessionManager gameSessionManager;

    private final Attack attack;
    private final Damage damage;
    private final PlayerService playerService;

    private final Map<UUID, Npc> npcs = new ConcurrentHashMap<>();

    @Autowired
    public NpcServiceImpl(@Lazy Attack attack, @Lazy Damage damage, PlayerService playerService) {
        this.attack = attack;
        this.damage = damage;
        this.playerService = playerService;
    }

    @Override
    public Npc spawnNpc(GameSession session) {

        GameMap map = session.getGameMap();
        Position startPosition = map.getRandomEmptyPosition();

        NpcMove npcMove = new NpcMove(this);
        NpcAttack npcAttack = new NpcAttack(this);
        NpcAiEngine npcAiEngine = new NpcAiEngine(npcMove, npcAttack, playerService);

        Npc npc = new Npc(getRandomNpcType(), startPosition, session, npcAiEngine);

        Thread npcThread = new Thread(npc);
        npcThread.start();

        npcs.put(npc.getId(), npc);
        return npc;
    }

    @Override
    public void move(UUID npcId, Direction direction) {
        Npc npc = getNpc(npcId);
        synchronized (npc) {

            GameSessionContext context = gameSessionManager.getContext(npc.getSession().getId());
            npc.getCommandQueue().add(new MoveCommand(direction, context, NORMAL_STEP));
        }
    }

    @Override
    public void attack(UUID npcId, UUID targetId) throws InterruptedException {
        Npc npc = getNpc(npcId);

        GameSessionContext context = gameSessionManager.getContext(npc.getSession().getId());

        Entity target = context.getEntitiesMap().get(targetId);

        if (target == null) {
            logger.info(LogMessages.TARGET_LEFT_SESSION);
            return;
        }
        npc.getCommandQueue().add(new AttackCommand(target.getId(), attack, context));
    }

    @Override
    public void defend(UUID npcId) {
        Npc npc = getNpc(npcId);
        npc.setDefending(true);
    }

    @Override
    public void takeDamage(UUID npcId, int amount) {
        Npc npc = getNpc(npcId);
        npc.getCommandQueue().add(new DamageCommand(amount, damage));
        if (!npc.isAlive()) {
            onDeath(npcId);
        }
    }

    @Override
    public void onDeath(UUID npcId) {
        Npc npc = getNpc(npcId);
        npc.disconnect();
    }

    @Override
    public Collection<Npc> getAllNpcs() {
        return npcs.values();
    }

    private Npc getNpc(UUID id) {
        return npcs.get(id);
    }

    private NpcType getRandomNpcType() {
        NpcType[] types = NpcType.values();
        return types[new Random().nextInt(types.length)];
    }
}
