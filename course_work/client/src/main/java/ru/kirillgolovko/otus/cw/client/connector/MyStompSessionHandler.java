package ru.kirillgolovko.otus.cw.client.connector;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;

/**
 * @author kirillgolovko
 */
public class MyStompSessionHandler implements StompSessionHandler {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("connected");
        session.subscribe("/topic/greeting", this);
        session.send("/app/greeting", "JOPA");

    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.out.println(exception);
        System.out.println("Handle ex");
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        System.out.println("Errr");
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("Received : " + payload);
    }
}
