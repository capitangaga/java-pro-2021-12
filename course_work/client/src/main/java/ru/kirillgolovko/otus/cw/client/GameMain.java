package ru.kirillgolovko.otus.cw.client;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import ru.kirillgolovko.cw.common.utils.ResourcePuller;
import ru.kirillgolovko.otus.cw.client.cli.GameLoop;
import ru.kirillgolovko.otus.cw.client.matchmaking.client.MatchmakerClient;
import ru.kirillgolovko.otus.cw.client.matchmaking.client.MatchmakerHttpClient;

import java.io.IOException;

/**
 * @author kirillgolovko
 */
public class GameMain {
    public static void main(String[] args) throws IOException{
        Terminal terminal = new DefaultTerminalFactory().createTerminal();
        ((SwingTerminalFrame) terminal).setTitle("PONG ONLINE");
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        MatchmakerClient matchmakerClient = new MatchmakerHttpClient("http://localhost", 8090);

        GameLoop gameLoop = new GameLoop(
                terminal,
                new ResourcePuller<>(matchmakerClient::getSession, 1000, 60000),
                stompClient);
        gameLoop.runLoop();
    }
}
