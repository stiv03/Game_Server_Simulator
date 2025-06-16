package org.example.config.messages;

public class LogMessages {

    public static final String STARTING_TOMCAT = "Starting Tomcat server on port {}";
    public static final String DEPLOYING_WEBAPP = "Deploying webapp from: {}";
    public static final String ERROR_STARTING_TOMCAT = "Error starting Tomcat";

    public static final String ATTACK_LOG = "{} attacked {}";

    public static final String INVALID_DTO = "Invalid DTO:{} file is null";

    public static final String USER_NOT_FOUND_BY_USERNAME = "User with username {} not found";
    public static final String FETCHING_USER_BY_USERNAME = "Fetching user by username: {}";
    public static final String ADDING_USER = "Creating user: {}";
    public static final String FETCHING_USER_BY_ID = "Fetching user by ID: {}";
    public static final String FETCHING_ALL_USERS = "Fetching all users";
    public static final String DELETING_USER = "Deleting user with ID: {}";
    public static final String UPDATING_USER = "Updating user: {}";
    public static final String USER_LEVEL_UP = " User {} level up to {} level.";
    public static final String USER_NOT_FOUND_BY_ID = "User with ID {} not found";
    public static final String UPDATED_USER = "User with ID {} successfully updated: {}";

    public static final String LOG_ITEM_PICKED = "Player {} picked up item {} at position ({}, {}).";
    public static final String LOG_HEALTH_RECOVERY = "Player {} healed";
    public static final String LOG_DOUBLE_DAMAGE = "Player {} gained DOUBLE DAMAGE ";
    public static final String LOG_INVINCIBILITY = "Player {} became INVINCIBLE";
    public static final String LOG_SPEED_BOOST = "Player {} gained SPEED_BOOST";
    public static final String LOG_ITEM_CONSUMED = "Item at position ({}, {}) has been consumed.";


    public static final String STARTING_SESSION = "Starting game session {}";
    public static final String PLAYER_JOIN = "Player {} joined session {}, at position {}";
    public static final String RANKING = "=== Ranking for session [{}] ===";
    public static final String ENTITY_RANKING = "#{} -> Name: {}, Health: {}";

    public static final String DISCONNECT_SESSION = "Player {} removed from session {}";
    public static final String PLAYER_NOT_IN_SESSION = "Tried to remove player {}, but not found in session {}";

    public static final String START_GAME_SESSION = "Game session {} started in new thread";
    public static final String INTERRUPTED_GAME_SESSION = "Game session thread interrupted for session {}";
    public static final String STOPPED_GAME_SESSION = "Game session {} stopped";
    public static final String STOP_ALL_ENTITIES_IN_SESSION = "All entities stopped in session {}";

    public static final String MOVE_TO = "{} moved {} to {}";
    public static final String DAMAGE_TAKEN = "{} took {} damage.";

    public static final String SPAWNED_NPC = "Spawning NPC at position {} in session {}";
    public static final String DROPPED_ITEM = "Item {} was dropper at position {} in session {}";

    public static final String TARGET_LEFT_SESSION = "Target left the session.";
    public static final String KILL_PLAYER_THREAD = "Player Thread is killed";

    public static final String PLAYER_IS_DEFENDING = "Player {} is defending";

    public static final String MAYBE_LEFT_SESSION = "Cannot attack: target is null (maybe left session).";
    public static final String TARGET_KILLED = "{} has been killed by {}";
}
