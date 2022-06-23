package ru.kirillgolovko.otus.cw.gameserver.session;

import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.kirillgolovko.cw.common.game.server.GameServer;
import ru.kirillgolovko.cw.common.game.server.GameServerSettings;
import ru.kirillgolovko.cw.common.model.KeyboardEvent;

import java.util.logging.Logger;

/**
 * @author kirillgolovko
 */
public class GameSession {
    private static final Logger logger = Logger.getLogger(GameSession.class.getName());
    private final String sessionId;
    private final RemoteClientConnector rightClient;
    private final RemoteClientConnector leftClient;
    private final GameServer gameServer;

    private volatile boolean leftConnected = false;
    private volatile boolean rightConnected = false;

    public GameSession(SimpMessagingTemplate simp, String sessionId) {
        this.sessionId = sessionId;
        this.rightClient = new RemoteClientConnector("/topic/state/" + sessionId + '/' + "r", simp);
        this.leftClient = new RemoteClientConnector("/topic/state/" + sessionId + '/' + "l", simp);
        this.gameServer = new GameServer(leftClient, rightClient, sessionId, GameServerSettings.DEFAULT_SETTINGS);
        logger.info("Created session " + sessionId);
    }

    public synchronized void connectClient(String side) {
        if (side.equals("r")) {
            rightConnected = true;
        }
        if (side.equals("l")) {
            leftConnected = true;
        }

        if (rightConnected && leftConnected && gameServer.getState().equals(Thread.State.NEW)) {
            logger.info("Starting server for session " + sessionId);
            gameServer.init();
            gameServer.start();
        }
    }

    public void acceptEvent(String side, KeyboardEvent event) {
        if (side.equals("r")) {
            rightClient.nextKeyboardEvent(event);
        }
        if (side.equals("l")) {
            leftClient.nextKeyboardEvent(event);
        }
    }

    public boolean isFinished() {
        return gameServer.getState().equals(Thread.State.TERMINATED);
    }
}
