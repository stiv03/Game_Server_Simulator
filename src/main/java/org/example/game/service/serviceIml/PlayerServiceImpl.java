package org.example.game.service.serviceIml;

import org.example.config.messages.LogMessages;
import org.example.game.enums.Direction;
import org.example.game.commands.AttackCommand;
import org.example.game.commands.DamageCommand;
import org.example.game.commands.MoveCommand;
import org.example.game.logic.Attack;
import org.example.game.logic.Damage;
import org.example.game.logic.gameSession.GameSessionContext;
import org.example.game.logic.gameSession.GameSessionManager;
import org.example.game.model.Entity;
import org.example.game.model.GameMap;
import org.example.game.model.Player;
import org.example.game.model.Position;
import org.example.game.service.PlayerService;
import org.example.persistence.entity.GameSession;
import org.example.persistence.entity.Users;
import org.example.persistence.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private static final int XP_NEEDED_TO_LEVEL_UP = 100;
    private static final int DEFENCE_DURATION_MILLIS = 30_000;
    private static final int NORMAL_STEP = 1;
    private static final int DOUBLE_STEP = 2;

    private final UsersService usersService;
    private final Attack attack;
    private final Damage damage;

    private final Map<UUID, Player> players = new ConcurrentHashMap<>();


    @Autowired
    private GameSessionManager gameSessionManager;

    @Autowired
    public PlayerServiceImpl(UsersService usersService, @Lazy Attack attack, @Lazy Damage damage) {
        this.usersService = usersService;
        this.attack = attack;
        this.damage = damage;
    }

    @Override
    public Player getPlayer(UUID id) {
        return players.get(id);
    }


    @Override
    public Player registerPlayer(Users user, GameSession session) {
        GameMap map = session.getGameMap();
        Position startPosition = map.getRandomEmptyPosition();
        Player player = new Player(user, startPosition, session);

        Thread playerThread = new Thread(player);
        playerThread.start();
        players.put(player.getId(), player);

        return player;
    }

    @Override
    public void move(UUID playerId, Direction direction) {
        Player player = getPlayer(playerId);

        GameSessionContext context = gameSessionManager.getContext(player.getSession().getId());

        refreshEffects(player);

        int steps = player.isSpeedBoost() ? DOUBLE_STEP : NORMAL_STEP;
        player.getCommandQueue().add(new MoveCommand(direction, context, steps));
    }


    @Override
    public void attack(UUID attackerId, UUID targetId) {
        Player attacker = getPlayer(attackerId);

        GameSessionContext context = gameSessionManager.getContext(attacker.getSession().getId());

        Entity target = context.getEntitiesMap().get(targetId);
        if (target == null) {
            logger.info(LogMessages.TARGET_LEFT_SESSION);
            return;
        }
        refreshEffects(attacker);
        attacker.getCommandQueue().add(new AttackCommand(target.getId(), attack, context));

    }

    @Override
    public void defend(UUID playerId) {
        Player player = getPlayer(playerId);
        logger.info(LogMessages.PLAYER_IS_DEFENDING, player.getName());
        player.setDefending(true);
        player.setDefendingEndTime(System.currentTimeMillis() + DEFENCE_DURATION_MILLIS);
    }

    @Override
    public void takeDamage(UUID playerId, int amount) {
        Player player = getPlayer(playerId);
        refreshEffects(player);
        player.getCommandQueue().add(new DamageCommand(amount, damage));
    }


    @Override
    public void gainXp(UUID playerId, int amount) {
        Player player = getPlayer(playerId);
        player.setXp(player.getXp() + amount);
        if (player.getXp() >= XP_NEEDED_TO_LEVEL_UP) {
            levelUp(player);
        }
    }

    @Override
    public void onDeath(UUID playerId) {
        Player player = getPlayer(playerId);
        player.disconnect();
    }

    @Override
    public void applyHealthRecovery(Player player, int amount, int maxHealth) {
        synchronized (player.getLock()) {
            player.setHealth(Math.min(maxHealth, player.getHealth() + amount));
        }
    }

    @Override
    public void applyDoubleDamage(Player player, long durationMillis) {
        synchronized (player.getLock()) {
            player.setDoubleDamage(true);
            player.setDoubleDamageEndTime(System.currentTimeMillis() + durationMillis);
        }
    }

    @Override
    public void applyInvincibility(Player player, long durationMillis) {
        synchronized (player.getLock()) {
            player.setInvincible(true);
            player.setInvincibilityEndTime(System.currentTimeMillis() + durationMillis);
        }
    }

    @Override
    public void applySpeedBoost(Player player, long durationMillis) {
        synchronized (player.getLock()) {
            player.setSpeedBoost(true);
            player.setSpeedBoostEndTime(System.currentTimeMillis() + durationMillis);
        }
    }

    private void levelUp(Player player) {
        usersService.levelUpUser(player.getUser().getId());
        player.setXp(0);
    }

    private void refreshEffects(Player player) {
        long now = System.currentTimeMillis();

        if (player.isDoubleDamage() && now > player.getDoubleDamageEndTime()) {
            player.setDoubleDamage(false);
        }

        if (player.isInvincible() && now > player.getInvincibilityEndTime()) {
            player.setInvincible(false);
        }

        if (player.isSpeedBoost() && now > player.getSpeedBoostEndTime()) {
            player.setSpeedBoost(false);
        }
    }

    @Override
    public Collection<Player> getAllPlayers() {
        return players.values();
    }
}
