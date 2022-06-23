package ru.kirillgolovko.otus.cw.client.connector;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import ru.kirillgolovko.cw.common.game.client.GameClient;
import ru.kirillgolovko.cw.common.model.GameFieldState;

/**
 * @author kirillgolovko
 */
public class StompClientConnector implements StompSessionHandler {

    private final GameClient gameClient;
    private final String sessionId;
    private final String side;

    private StompSession session;

    public StompClientConnector(
            GameClient gameClient,
            String sessionId,
            String side)
    {
        this.gameClient = gameClient;
        this.sessionId = sessionId;
        this.side = side;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        this.session = session;
        gameClient.addKeyboardEventsConsumer(event -> session.send(getPushEndpoint(), event));
        gameClient.startGame();
    }

    private String getPushEndpoint() {
        return String.format("/app/keyboard_events/%s/%s", session, side);
    }

    @Override
    public void handleException(
            StompSession session,
            StompCommand command,
            StompHeaders headers,
            byte[] payload,
            Throwable exception)
    {

    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {

    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return GameFieldState.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        GameFieldState gameFieldState = (GameFieldState) payload;
        gameClient.updateFieldState(gameFieldState);
    }
}
