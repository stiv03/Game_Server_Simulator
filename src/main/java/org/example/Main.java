package org.example;

import org.apache.catalina.LifecycleException;
import org.example.config.webapp.TomcatServer;
import org.example.game.logic.gameSession.GameSessionContext;
import org.example.game.model.GameMap;
import org.example.persistence.entity.GameSession;
import org.example.persistence.entity.enums.GameDifficulty;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.example")
public class Main {

    public static void main(String[] args) throws LifecycleException {
//        GameMap gameMap = new GameMap();
//        gameMap.printToConsole();

//                GameSession session = new GameSession();
//                session.setDifficulty(GameDifficulty.NORMAL);
//        GameSessionContext context = new GameSessionContext(session);

        TomcatServer.start();

//        GameSession session = new GameSession();
//        GameSessionContext context = new GameSessionContext(session);
//        context.printMapWithEntities();
    }
}