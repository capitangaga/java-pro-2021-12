package ru.kirillgolovko.otus.cw.client.connector;

import java.io.IOException;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.kirillgolovko.otus.cw.client.cli.TerminalGame;

/**
 * @author kirillgolovko
 */
public class TestConnection {
    public static void main(String[] args) throws InterruptedException , IOException {

        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        Terminal left = defaultTerminalFactory.createTerminal();
        Terminal right = defaultTerminalFactory.createTerminal();
        TerminalGame leftGame = new TerminalGame(left);
        TerminalGame rightGame = new TerminalGame(right);

        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        stompClient.connect("ws://localhost:8080/connect", new StompClientConnector(rightGame, "1", "r"));
//        Thread.sleep(100000);
    }
}
