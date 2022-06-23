package ru.kirillgolovko.otus.cw.gameserver.session;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.kirillgolovko.cw.common.game.server.GameServer;
import ru.kirillgolovko.cw.common.game.server.GameServerSettings;
import ru.kirillgolovko.cw.common.model.KeyboardEvent;

/**
 * @author kirillgolovko
 */
public class GameSession {
    private final String sessionId;
    private final RemoteClientConnector rightClient;
    private final RemoteClientConnector leftClient;
    private final GameServer gameServer;

    private volatile boolean leftConnected = false;
    private volatile boolean rightConnected = false;

    public GameSession(SimpMessagingTemplate simp, String sessionId) {
        this.sessionId = sessionId;
        this.rightClient = new RemoteClientConnector(sessionId + '/' + "r", simp);
        this.leftClient = new RemoteClientConnector(sessionId + '/' + "l", simp);
        this.gameServer = new GameServer(leftClient, rightClient, sessionId, GameServerSettings.DEFAULT_SETTINGS);
    }

    public synchronized void connectClient(String side) {
        if (side.equals("r")) {
            rightConnected = true;
        }
        if (side.equals("l")) {
            leftConnected = true;
        }

        if (rightConnected && leftConnected && gameServer.getState().equals(Thread.State.RUNNABLE)) {
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
