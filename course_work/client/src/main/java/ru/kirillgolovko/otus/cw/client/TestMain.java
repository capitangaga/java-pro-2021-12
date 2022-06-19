package ru.kirillgolovko.otus.cw.client;

import java.io.IOException;
import java.util.List;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import ru.kirillgolovko.cw.common.game.server.GameServer;
import ru.kirillgolovko.cw.common.game.server.GameServerSettings;
import ru.kirillgolovko.cw.common.model.GameFieldState;
import ru.kirillgolovko.cw.common.model.Point;
import ru.kirillgolovko.otus.cw.client.cli.TerminalGame;

/**
 * @author kirillgolovko
 */
public class TestMain {
    public static void main(String[] args) throws IOException, InterruptedException {
//        List<Point> pointsToExplore = List.of(new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 0));
//        Terminal terminal = new DefaultTerminalFactory().createTerminal();
//        TerminalGame terminalGame = new TerminalGame(terminal);
//        terminalGame.addKeyboardEventsConsumer(System.out::println);
//        terminalGame.startGame();
//
//        int i = 0;
//        while (true) {
//            ++ i;
//            Point point = pointsToExplore.get(i % 4);
//            GameFieldState gameFieldState = new GameFieldState(point, 0.5, 0.5, 0, 0);
//            terminalGame.updateFieldState(gameFieldState);
//            Thread.sleep(1000);
//        }

        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        Terminal left = defaultTerminalFactory.createTerminal();
        Terminal right = defaultTerminalFactory.createTerminal();
        TerminalGame leftGame = new TerminalGame(left);
        TerminalGame rightGame = new TerminalGame(right);
        leftGame.startGame();
        rightGame.startGame();
        GameServer gameServer = new GameServer(leftGame, rightGame, "", GameServerSettings.DEFAULT_SETTINGS);
        gameServer.init();
        gameServer.start();
        gameServer.join();

    }
}
