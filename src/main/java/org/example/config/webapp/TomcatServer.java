package org.example.config.webapp;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.example.config.messages.LogMessages;
import org.example.exceptions.WebAppNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class TomcatServer {

    private static final Logger logger = LoggerFactory.getLogger(TomcatServer.class);
    private static final int PORT = 8080;
    private static final String WEBAPP_DIR = "target/Game_Server_Simulator-1.0-SNAPSHOT.war";

    public static void start() throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(PORT);

        File webAppDir = new File(WEBAPP_DIR);

        if (!webAppDir.exists()) {
            logger.error(LogMessages.ERROR_STARTING_TOMCAT);
            throw new WebAppNotFoundException(webAppDir.getAbsolutePath());
        }

        logger.info(LogMessages.DEPLOYING_WEBAPP, webAppDir.getAbsolutePath());
        tomcat.addWebapp("", webAppDir.getAbsolutePath());

        logger.info(LogMessages.STARTING_TOMCAT, tomcat.getConnector().getPort());
        tomcat.start();
        tomcat.getServer().await();
    }
}